package com.kob.backend.controller.friendships;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.friendships.CreateFriendshipsService;
import com.kob.backend.service.friendships.GetFriendshipsListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class FriendshipsController {
    @Autowired
    private CreateFriendshipsService createFriendshipsService;
    @Autowired
    private GetFriendshipsListService getFriendshipsListService;

    @PostMapping("/api/friendships/create/")
    public Map<String, String> create(@RequestParam Map<String, String> data) {
        Integer user1Id = Integer.parseInt(data.get("user1Id"));
        Integer user2Id = Integer.parseInt(data.get("user2Id"));

        return createFriendshipsService.create(user1Id, user2Id);
    }

    @GetMapping("/api/friend/getlist/")
    public JSONObject getList(@RequestParam Map<String, String> data) {
        Integer userId = Integer.valueOf(data.get("userId"));

        return getFriendshipsListService.getList(userId);
    }
}
