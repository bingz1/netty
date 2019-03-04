package henu.zhang.netty.server.entity;

/**
 * @author 张向兵
 */
public class MessageType {

    public static int LOGIN = 1; //登录服务器

    public static int JOIN_ROOM = 2; //加入直播间

    public static int LEAVE_ROOM = 3; //离开直播间

    public static int LOGOUT = 4; //登出服务器


    public static int SINGLE_CHAT = 5; //向指定用户发送消息

    public static int SINGLE_ROOM = 6; //向指定房间发送消息

    public static int ALL_ROOM = 7; //向所有房间发送消息

    public static int ALL_CLIENTS = 8; //向所有在线用户发送消息

}
