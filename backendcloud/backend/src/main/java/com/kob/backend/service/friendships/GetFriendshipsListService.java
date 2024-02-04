package com.kob.backend.service.friendships;

import com.alibaba.fastjson.JSONObject;

public interface GetFriendshipsListService {
    public JSONObject getList(Integer userId, String query);
}
