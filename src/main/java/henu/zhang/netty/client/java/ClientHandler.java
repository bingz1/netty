package henu.zhang.netty.client.java;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * @author zxb
 * @date 2016/10/3
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 接收server端的消息，并打印出来
     *
     * @param ctx 链接信息
     * @param msg 传输信息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {

        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        String body = new String(bytes, "UTF-8");
        System.out.println(body);

    }

    /**
     * 连接成功后，向server发送消息
     *
     * @param ctx 链接信息
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        byte[] auth = "我是消息".getBytes();

        ByteBuf encoded = ctx.alloc().buffer(auth.length);
        encoded.writeBytes(auth);
        ctx.writeAndFlush(encoded);
    }

    /**
     * 捕获并处理 异常信息.
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }
}
