package com.kob.backend.service.impl.friendships;

import com.kob.backend.mapper.FriendshipsMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Friendships;
import com.kob.backend.pojo.User;
import com.kob.backend.service.friendships.CreateFriendshipsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CreateFriendshipsServiceImpl implements CreateFriendshipsService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FriendshipsMapper friendshipsMapper;

    @Override
    public Map<String, String> create(Integer user1Id, Integer user2Id) {
        User user1 = userMapper.selectById(user1Id);
        User user2 = userMapper.selectById(user2Id);

        Map<String, String> resp = new HashMap<>();
        if(user1 == null || user2 == null) {
            resp.put("result", "操作不当!");
            return resp;
        }

        friendshipsMapper.insert(new Friendships(
                null,
                user1Id,
                user2Id,
                new Date()
        ));

        resp.put("result", "success");

        return resp;
    }
}
