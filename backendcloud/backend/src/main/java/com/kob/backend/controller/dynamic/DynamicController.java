package com.kob.backend.controller.dynamic;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.dynamic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DynamicController {
    @Autowired
    private CreateDynamicService createDynamicService;
    @Autowired
    private GetDynamicListService getDynamicListService;
    @Autowired
    private DeleteDynamicService deleteDynamicService;
    @Autowired
    private GiveALikeService giveALikeService;
    @Autowired
    private GetAidDynamicService getAidDynamicService;

    @PostMapping("/api/dynamic/create/")
    public Map<String, String> create(@RequestParam Map<String, String> data) {
        Integer userId = Integer.parseInt(data.get("userId"));
        Integer replyId = Integer.parseInt(data.get("replyId"));
        String content = data.get("content");
        String photos = data.get("photos");

        return createDynamicService.create(userId, replyId, content, photos);
    }

    @GetMapping("/api/dynamic/getlist/")
    public JSONObject getlist(@RequestParam Map<String, String> data) {
        Integer user_id = Integer.parseInt(data.get("userId"));
        Integer page = Integer.parseInt(data.get("page"));

        return getDynamicListService.getList(user_id, page);
    }

    @GetMapping("/api/dynamic/delete/")
    public Map<String, String> delete(@RequestParam Map<String, String> data) {
        Integer dynamicId = Integer.parseInt(data.get("dynamicId"));

        return deleteDynamicService.delete(dynamicId);
    }

    @PostMapping("/api/dynamic/give/like/")
    public Map<String, String> giveLike(@RequestParam Map<String, String> data) {
        Integer dynamicId = Integer.parseInt(data.get("dynamicId"));
        Integer num = Integer.parseInt(data.get("num"));

        return giveALikeService.giveLike(dynamicId, num);
    }

    @GetMapping("/api/dynamic/getAid/")
    public JSONObject getAid(@RequestParam Map<String, String> data) {
        Integer dynamicId = Integer.valueOf(data.get("dynamicId"));

        return getAidDynamicService.getAid(dynamicId);
    }
}
