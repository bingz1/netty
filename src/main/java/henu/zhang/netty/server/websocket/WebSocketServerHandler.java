package henu.zhang.netty.server.websocket;

import com.alibaba.fastjson.JSON;
import henu.zhang.netty.server.entity.AllClients;
import henu.zhang.netty.server.entity.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpHeaderNames.HOST;

import java.util.List;
import java.util.Map;

public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    private WebSocketServerHandshaker handshaker;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {

        if (msg instanceof FullHttpRequest) {    // HTTP接入，WebSocket第一次连接使用HTTP连接,用于握手
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {  //Websocket 接入
            handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    //处理http请求
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {

        if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        //获取连接中附带的参数
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(req.uri());
        Map<String, List<String>> parameters = queryStringDecoder.parameters();
        String token = parameters.get("token").get(0);

        if ("abc".equalsIgnoreCase(token)) {  //如果验证通过  将客户端保留在内存中
            AllClients.allClients.put(token, ctx.channel());
        }else {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED));
            return;
        }

        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketLocation(req), null, false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {

        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }

        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static String getWebSocketLocation(FullHttpRequest req) {
        String location = req.headers().get(HOST);
        return "ws://" + location;
    }

    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {

        if (frame instanceof CloseWebSocketFrame) {   //客户端主动关闭连接
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
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
            String request = ((TextWebSocketFrame) frame).text();

            //处理收到的消息
            handleRequest(request);

            System.out.println("服务端收到：" + request);
            //给客户端返回接收状态
            ctx.channel().write(new TextWebSocketFrame("服务器收到并返回：" + request));
        }

    }

    private void handleRequest(String request){

        Message message = JSON.parseObject(request,Message.class);
        switch (message.getType()){
            case 1:
                System.out.println(message.getMessage());
                break;
            case 2:
                System.out.println(message.getMessage());
                break;
            case 3:
                System.out.println(message.getMessage());
                break;
            case 4:
                System.out.println(message.getMessage());
                break;
            case 5:
                System.out.println(message.getMessage());
                break;
            case 6:
                System.out.println(message.getMessage());
                break;
            case 7:
                System.out.println(message.getMessage());
                break;
            case 8:
                System.out.println(message.getMessage());
                break;
            case 9:
                System.out.println(message.getMessage());
                break;
            default:
                System.out.println(message.getMessage());
                break;
        }
    }
}