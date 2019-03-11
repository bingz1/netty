package henu.zhang.netty.client.java;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zxb
 * @date 2016/10/3
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 接收server端的消息，并打印出来
     * @param ctx 链接信息
     * @param msg 传输信息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 接收客户端的数据
        ByteBuf buf = (ByteBuf) msg;

        //设置字节数组大小
        byte[] message = new byte[buf.readableBytes()];
        //将缓冲区的的字节数组复制到新建的数组之中

        buf.readBytes(message);

        System.out.println(ByteBufUtil.hexDump(message));

        buf.release();

    }

    /**
     * 连接成功后，向server发送消息
     * @param ctx 链接信息
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        byte[] auth = "我是消息".getBytes();

        ByteBuf encoded = ctx.alloc().buffer(auth.length);
        encoded.writeBytes(auth);
        ctx.writeAndFlush(encoded);
    }

}
