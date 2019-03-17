package henu.zhang.netty.server.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.Date;

/**
 * @author 张向兵
 */
public class HeartBeatServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 心跳丢失计数器
     */
    private int lossConnectCount = 0;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("已经5秒未收到客户端的消息了！");
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.ALL_IDLE) {
                lossConnectCount++;
                if (lossConnectCount > 2) {
                    System.out.println("关闭这个不活跃通道！");
                    ctx.channel().close();
                }
            }

            ctx.channel().write(new TextWebSocketFrame("服务器收到并返回：" + new Date()));
            ctx.flush();
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
