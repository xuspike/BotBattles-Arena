package com.kob.backend.controller.notice;

import com.kob.backend.service.notice.ChangeDynamicNoticeStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DynamicNoticeController {
    @Autowired
    private ChangeDynamicNoticeStatusService changeDynamicNoticeStatusService;

    @PostMapping("/api/notice/dynamic/status/change/")
    public Map<String, String> changeStatus(@RequestParam Map<String, String> data) {
        String noticeIds = data.get("noticeIds");

        return changeDynamicNoticeStatusService.changeStatus(noticeIds);
    }
}
