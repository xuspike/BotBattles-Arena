package com.kob.backend.service.message;

import com.alibaba.fastjson.JSONObject;

public interface GetMessageListService {
    public JSONObject getList(Integer friendshipId, Integer page);
}
