package com.kob.backend.service.impl.message;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.FriendshipsMapper;
import com.kob.backend.mapper.MessageMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Friendships;
import com.kob.backend.pojo.Message;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.message.GetMessageListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetMessageListServiceImpl implements GetMessageListService {
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private FriendshipsMapper friendshipsMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getList(Integer friendshipId, Integer page) {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();

        Friendships friendships = friendshipsMapper.selectById(friendshipId);

        JSONObject resp = new JSONObject();
        if(user.getId() != friendships.getUser1Id() && user.getId() != friendships.getUser2Id()) {
            resp.put("result", "你没有该权限！");
            return resp;
        }

        IPage<Message> messageIPage = new Page<>(page, 10);
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("friendship_id", friendshipId).orderByAsc("id"); // 消息是从上到下渲染
        List<Message> messages = messageMapper.selectPage(messageIPage, queryWrapper).getRecords();

        List<JSONObject> items = new ArrayList<>();
        for(Message message : messages) {
            JSONObject item = new JSONObject();

            User user1 = userMapper.selectById(message.getSenderId());
            User user2 = userMapper.selectById(message.getReceiverId());
            item.put("sender", user1);
            item.put("receiver", user2);
            item.put("message", message);
            items.add(item);
        }

        resp.put("messageCount", messageMapper.selectCount(queryWrapper));
        resp.put("messages", items);
        resp.put("result", "success");

        return resp;
    }
}