package henu.zhang.netty.server.entity;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AllClients {

    //所有的客户端信息
    public static Map<String, Channel> allClients = new ConcurrentHashMap<>();
}
