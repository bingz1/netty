package henu.zhang.netty.server.websocket;

import henu.zhang.netty.server.entity.Clients;
import henu.zhang.netty.server.service.HandleMessageService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.netty.channel.ChannelHandler.Sharable;

import static io.netty.handler.codec.http.HttpHeaderNames.HOST;

import java.util.List;
import java.util.Map;


/**
 * @author 张向兵
 */
@Component
@Sharable
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    private WebSocketServerHandshaker handShaker;

    @Autowired
    private HandleMessageService handleMessageService;

    @Autowired
    private Clients clients;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {

        /**
         *  WebSocket第一次连接使用HTTP连接,用于握手
         *  Websocket 接入
         */
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    /**
     * 处理异常关闭情况.
     * 将客户端从连接信息中删除
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 处理http请求
     *
     * @param ctx
     * @param req
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {

        if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        //获取连接中附带的参数
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(req.uri());
        Map<String, List<String>> parameters = queryStringDecoder.parameters();
        String token = parameters.get("token").get(0);

        /**
         * 如果验证通过  将客户端保留在内存中
         * abc 生产上应当从数据库中读取
         */
        if ("token".equalsIgnoreCase(token)) {
            clients.put(token, ctx);
        } else {
            sendHttpResponse(ctx, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED));
            return;
        }

        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketLocation(req), null, false);
        handShaker = wsFactory.newHandshaker(req);
        if (handShaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handShaker.handshake(ctx.channel(), req);
        }
    }

    /**
     * HTTP请求响应
     *
     * @param ctx
     * @param res
     */
    private static void sendHttpResponse(ChannelHandlerContext ctx, DefaultFullHttpResponse res) {

        // 返回应答给客户端
        if (res.status().code() != HttpResponseStatus.OK.code()) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }

        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (res.status().code() != HttpResponseStatus.OK.code()) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static String getWebSocketLocation(FullHttpRequest req) {
        String location = req.headers().get(HOST);
        return "ws://" + location;
    }

    /**
     * 处理websocket类型消息
     *
     * @param ctx
     * @param frame
     */
    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {

        /**
         * 客户端主动关闭连接
         */
        if (frame instanceof CloseWebSocketFrame) {
            handShaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }

        /**
         * 判断是否ping消息
         */
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        /**
         * 本例程仅支持文本消息，不支持二进制消息
         */
        if (frame instanceof BinaryWebSocketFrame) {
            throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass().getName()));
        }

        if (frame instanceof TextWebSocketFrame) {
            // 收到的消息
            String message = ((TextWebSocketFrame) frame).text();
            handleMessageService.handleClientMessage(ctx, message);
        }

    }
}