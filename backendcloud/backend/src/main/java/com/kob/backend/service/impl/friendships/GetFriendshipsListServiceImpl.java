package com.kob.backend.service.impl.friendships;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.FriendshipsMapper;
import com.kob.backend.mapper.MessageMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Friendships;
import com.kob.backend.pojo.Message;
import com.kob.backend.pojo.User;
import com.kob.backend.service.friendships.GetFriendshipsListService;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetFriendshipsListServiceImpl implements GetFriendshipsListService {
    @Autowired
    private FriendshipsMapper friendshipsMapper;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getList(Integer userId) {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();

        JSONObject resp = new JSONObject();
        if(user.getId() != userId) {
            resp.put("result", "你没有该权限！");
            return resp;
        }

        List<Integer> friendIds = friendshipsMapper.findAllFriendsByUserId(userId);
        List<JSONObject> items = new ArrayList<>();
        for(Integer friendId : friendIds) {
            JSONObject item = new JSONObject();
            QueryWrapper<Friendships> friendshipsQueryWrapper = new QueryWrapper<>();
            friendshipsQueryWrapper.nested(i -> i.eq("user1_id", userId).eq("user2_id", friendId)).or()
                    .nested(i -> i.eq("user1_id", friendId).eq("user2_id", userId)).orderByDesc("last_timestamp");
            Friendships friendship = friendshipsMapper.selectOne(friendshipsQueryWrapper);

            Message last_message = messageMapper.selectById(friendship.getLastMsgId());
            User friend = userMapper.selectById(friendId);
            item.put("lastMessage", last_message);
            item.put("friend", friend);
            item.put("friendship", friendship);
            items.add(item);
        }
        resp.put("friends", items);
        resp.put("result", "success");

        return resp;
    }
}
