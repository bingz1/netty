package henu.zhang.netty.server.entity;

public class Message {

    private int type;   //消息类型

    private String target;  //目标用户

    private String message; //消息内容  后续可能需要定义成实体类 来区分不同的消息类型

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
