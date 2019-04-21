package henu.zhang.netty.server.entity;

import lombok.Data;

/**
 * @author 张向兵
 */
@Data
public class Msg {

    /**
     * 消息类型
     */
    private int type;

    /**
     * 目标用户
     */
    private String target;

    /**
     * 消息内容  后续可能需要定义成实体类 来区分不同的消息类型
     */
    private String message;


}
