package com.kob.backend.consumer;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.FriendshipsMapper;
import com.kob.backend.mapper.MessageMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Friendships;
import com.kob.backend.pojo.Message;
import com.kob.backend.pojo.User;
import com.kob.backend.service.message.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/single/{token}")  // 注意不要以'/'结尾
public class WebSocketSingleServer {
    // ConcurrentHashMap是线程安全的哈希表
    // 让每一个实例访问同一个users（存储目前所有的连接）
    final public static ConcurrentHashMap<Integer, WebSocketSingleServer> users = new ConcurrentHashMap<>();
    // 临时匹配池，CopyOnWriteArraySet是线程安全的Set
    private User user; // 当前连接的用户
    private Session session = null; // 每个连接用session来维护

    public static UserMapper userMapper;

    public static MessageMapper messageMapper;

    public static FriendshipsMapper friendshipsMapper;

    public static RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        WebSocketSingleServer.restTemplate = restTemplate;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketSingleServer.userMapper = userMapper;
    }

    @Autowired
    public void setMessageMapper(MessageMapper messageMapper) {
        WebSocketSingleServer.messageMapper = messageMapper;
    }

    @Autowired
    private void setFriendshipsMapper(FriendshipsMapper friendshipsMapper) {
        WebSocketSingleServer.friendshipsMapper = friendshipsMapper;
    }

    private Message SendMessageToDataBase(Integer friendshipId ,Integer senderId, Integer receiverId, String content) {
        Message new_message = new Message(
                null,
                friendshipId,
                senderId,
                receiverId,
                content,
                new Date()
        );

        messageMapper.insert(new_message);

        return new_message;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session = session;
        // JwtAuthentication是再consumer/utils中封装的类，用token判断用户是否存在
        Integer userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);

        if(this.user != null) {
            users.put(userId, this);
        } else {
            this.session.close();
        }
    }

    @OnClose
    public void onClose() {
        // 关闭连接
        if(this.user != null) {
            users.remove(this.user.getId());
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) { // 一般当做路由，判断把任务交给谁处理
        // 从Client接收消息
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        // 反过来调用equals()，可避免event为空时抛出异常
        if("sendMessage".equals(event)) {
            Integer friendshipId = Integer.valueOf(data.getString("friendshipId"));
             Integer senderId = Integer.valueOf(data.getString("senderId"));
             Integer receiverId = Integer.valueOf(data.getString("receiverId"));
             String content = data.getString("content");

             WebSocketSingleServer sender = users.get(senderId);
             WebSocketSingleServer receiver = users.get(receiverId);

             Friendships friendship = friendshipsMapper.selectById(friendshipId);
             Message new_message = SendMessageToDataBase(friendshipId, senderId, receiverId, content);
             JSONObject item = new JSONObject();
             item.put("message", new_message);
             item.put("sender", userMapper.selectById(senderId));
             item.put("receiver", userMapper.selectById(receiverId));
             item.put("event", "receive message");
             Integer msgCount1 = 0, msgCount2 = 0;
             if(sender != null) sender.sendMessage(item.toJSONString());
             if(receiver != null) receiver.sendMessage(item.toJSONString());
             else {
                 if(receiverId == friendship.getUser1Id()) msgCount1 ++;
                 else msgCount2 ++;
             }

             friendshipsMapper.updateById(
                     new Friendships(
                             friendshipId,
                             friendship.getUser1Id(),
                             friendship.getUser2Id(),
                             new_message.getId(),
                             BigInteger.valueOf(new_message.getCreatetime().getTime()),
                             friendship.getMsgCnt1() + msgCount1,
                             friendship.getMsgCnt2() + msgCount2,
                             friendship.getCreatetime()
                     )
             );
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message) {
        // 异步请求要上锁
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
