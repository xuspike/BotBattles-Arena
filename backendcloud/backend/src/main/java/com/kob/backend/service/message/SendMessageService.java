package com.kob.backend.service.message;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface SendMessageService {
    public JSONObject sendMessage(Integer friendshipId, Integer senderId, Integer receiverId, String content);
}
