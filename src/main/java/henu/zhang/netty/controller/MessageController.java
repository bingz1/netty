package henu.zhang.netty.controller;

import henu.zhang.netty.server.service.HandleMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 张向兵
 * @date 2019-03-02.
 */
@RestController
public class MessageController {

    @Autowired
    private HandleMessageService handleMessageService;

    @GetMapping("send")
    public String getName() {
        handleMessageService.handleServerMessage("token", "我是服务器主动发的消息");
        return "张向兵";
    }

}
