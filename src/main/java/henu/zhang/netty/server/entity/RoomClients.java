package henu.zhang.netty.server.entity;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RoomClients {

    public static Map<String, ChannelGroup> roomClients = new ConcurrentHashMap<>();

    //加入房间
    public void joinRoom(String roomID, Channel channel) {
        if (!roomClients.containsKey(roomID)) {
            roomClients.put(roomID, new DefaultChannelGroup(GlobalEventExecutor.INSTANCE));
        }
        ChannelGroup channelGroup = roomClients.get(roomID);
        channelGroup.add(channel);
    }

    //离开房间
    public void leaveRoom(String roomID, Channel channel) {
        ChannelGroup channelGroup = roomClients.get(roomID);
        channelGroup.remove(channel);
    }

    //销毁房间
    public void destroyRoom(String roomID){   //主播关播时  将主播的聊天群销毁
        ChannelGroup channelGroup = roomClients.get(roomID);
        channelGroup.clear();  //清空这个房间

        roomClients.remove(roomID);
    }
}
