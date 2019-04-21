package henu.zhang.netty.server.mq.redis;

import com.alibaba.fastjson.JSONObject;
import henu.zhang.netty.server.entity.Msg;
import henu.zhang.netty.server.service.HandleMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

/**
 * @author 张向兵
 * @date 2019-04-21.
 */
@Service
public class RedisMessageSubscriber implements MessageListener {

    @Autowired
    private HandleMessageService handleMessageService;

    @Override
    public void onMessage(Message message, byte[] bytes) {

        Msg msg = JSONObject.parseObject(message.toString(), Msg.class);
        handleMessageService.handleServerMessage(msg.getTarget(), msg.getMessage());

    }
}
