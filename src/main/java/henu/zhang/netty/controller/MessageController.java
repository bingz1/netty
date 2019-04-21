package henu.zhang.netty.controller;

import com.alibaba.fastjson.JSON;
import henu.zhang.netty.server.entity.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 张向兵
 * @date 2019-03-02.
 */
@RestController
public class MessageController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ChannelTopic topic;

    @PostMapping("send")
    public String getName(@RequestBody Msg msg) {
        redisTemplate.convertAndSend(topic.getTopic(), msg);
        return "张向兵";
    }

}
