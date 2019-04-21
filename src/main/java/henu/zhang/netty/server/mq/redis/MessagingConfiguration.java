package henu.zhang.netty.server.mq.redis;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author 张向兵
 * @date 2019-04-21.
 */
@Configuration
public class MessagingConfiguration {

    /**
     * 重写 RedisTemplate 对象,使用fastJson封装
     *
     * @param factory redis连接工厂类
     * @return 新的RedisTemplate
     */
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {

        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(factory);

        GenericFastJsonRedisSerializer fastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
        template.setKeySerializer(new StringRedisSerializer());
        template.setDefaultSerializer(fastJsonRedisSerializer);

        return template;
    }

    @Bean
    ChannelTopic topic() {
        return new ChannelTopic("second");
    }

    @Autowired
    RedisMessageSubscriber redisMessageSubscriber;

    @Bean
    MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(redisMessageSubscriber);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(RedisConnectionFactory factory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addMessageListener(messageListener(), topic());
        return container;
    }
}
