package com.kob.backend.controller.notice;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.notice.ChangeFriendNoticeStatusService;
import com.kob.backend.service.notice.GetFriendNoticeListService;
import com.kob.backend.service.notice.SendFriendNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class FriendNoticeController {
    @Autowired
    private SendFriendNoticeService sendFriendNoticeService;
    @Autowired
    private GetFriendNoticeListService getFriendNoticeListService;
    @Autowired
    private ChangeFriendNoticeStatusService changeFriendNoticeStatusService;

    @PostMapping("/api/friend/notice/send/")
    public Map<String, String> sendFriendNotice(@RequestParam Map<String, String> data) {
        Integer senderId = Integer.valueOf(data.get("senderId"));
        Integer receiverId = Integer.valueOf(data.get("receiverId"));

        return sendFriendNoticeService.sendFriendNotice(senderId, receiverId);
    }

    @GetMapping("/api/friend/notice/getlist/")
    public JSONObject getList(@RequestParam Map<String, String> data) {
        Integer userId = Integer.valueOf(data.get("userId"));
        Integer page = Integer.valueOf(data.get("page"));

        return getFriendNoticeListService.getList(userId, page);
    }

    @PostMapping("/api/friend/notice/isAccept/")
    public Map<String, String> ChangeStatus(@RequestParam Map<String, String> data) {
        Integer noticeId = Integer.valueOf(data.get("noticeId"));
        Integer operation = Integer.valueOf(data.get("operation"));

        return changeFriendNoticeStatusService.changeStatus(noticeId, operation);
    }

}
