package henu.zhang.netty.server.service;

import henu.zhang.netty.server.entity.Clients;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 张向兵
 */
@Service
public class HandleMessageService {

    @Autowired
    private Clients clients;

    /**
     * 处理客户端主动发过来的消息.
     *
     * @param ctx     连接信息
     * @param message 消息信息.
     */
    public void handleClientMessage(ChannelHandlerContext ctx, String message) {
        System.out.println("服务端收到：" + message);
        //给客户端返回接收状态
        ctx.channel().write(new TextWebSocketFrame("服务器收到并返回：" + message));
    }


    /**
     * 处理服务端主动推送的消息
     *
     * @param token
     * @param message
     */
    public void handleServerMessage(String token, String message) {
        ChannelHandlerContext ctx = clients.get(token);
        if (ctx != null) {
            ctx.channel().write(new TextWebSocketFrame("服务器主动消息：" + message));
        }
    }
}
