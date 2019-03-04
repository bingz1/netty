package henu.zhang.netty.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 张向兵
 * @date 2019-03-02.
 */
@RestController
public class Test {

    @GetMapping("test")
    public String getName(){
        return "张向兵";
    }

}
