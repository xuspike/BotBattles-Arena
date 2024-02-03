package com.kob.backend.service.impl.message;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.mapper.FriendshipsMapper;
import com.kob.backend.mapper.MessageMapper;
import com.kob.backend.pojo.Friendships;
import com.kob.backend.pojo.Message;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.message.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SendMessageServiceImpl implements SendMessageService {
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private FriendshipsMapper friendshipsMapper;
    @Override
    public JSONObject sendMessage(Integer friendshipId, Integer senderId, Integer receiverId, String content) {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();

        JSONObject resp = new JSONObject();

        Friendships friendship = friendshipsMapper.selectById(friendshipId);
        if(friendship == null) {
            resp.put("result", "操作不当！");
            return resp;
        } else {
            int user1Id = friendship.getUser1Id(), user2Id = friendship.getUser2Id();
            if((user1Id != senderId || user2Id != receiverId) && (user1Id != receiverId || user2Id != senderId)) {
                resp.put("result", "操作不当！");
                return resp;
            }
        }
        if(user.getId() != senderId) {
            resp.put("result", "你没有该权限！");
            return resp;
        }

        Message new_message = new Message(
                null,
                friendshipId,
                senderId,
                receiverId,
                content,
                new Date()
        );

        messageMapper.insert(new_message);

        resp.put("message", new_message);
        resp.put("result", "success");
        return resp;
    }
}
