package com.kob.backend.controller.message;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.message.GetMessageListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MessageController {
    @Autowired
    private GetMessageListService getMessageListService;

    @GetMapping("/api/get/friend/messages/")
    public JSONObject getList(@RequestParam Map<String, String> data) {
        Integer friendshipId = Integer.valueOf(data.get("friendshipId"));
        Integer page = Integer.valueOf(data.get("page"));

        return getMessageListService.getList(friendshipId, page);
    }
}
