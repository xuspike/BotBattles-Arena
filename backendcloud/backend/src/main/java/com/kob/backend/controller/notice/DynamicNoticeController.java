package com.kob.backend.controller.notice;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.notice.ChangeDynamicNoticeStatusService;
import com.kob.backend.service.notice.GetDynamicNoticeListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DynamicNoticeController {
    @Autowired
    private ChangeDynamicNoticeStatusService changeDynamicNoticeStatusService;
    @Autowired
    private GetDynamicNoticeListService getDynamicNoticeListService;
    @PostMapping("/api/notice/dynamic/status/change/")
    public Map<String, String> changeStatus(@RequestParam Map<String, String> data) {
        Integer noticeId = Integer.valueOf(data.get("noticeId"));

        return changeDynamicNoticeStatusService.changeStatus(noticeId);
    }

    @GetMapping("/api/notice/dynamic/getlist/")
    public JSONObject getList(@RequestParam Map<String, String> data) {
        Integer userId = Integer.parseInt(data.get("userId"));
        Integer page = Integer.parseInt(data.get("page"));

        return getDynamicNoticeListService.getList(userId, page);
    }

}
