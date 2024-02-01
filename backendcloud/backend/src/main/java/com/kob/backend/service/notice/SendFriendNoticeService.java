package com.kob.backend.service.notice;

import java.util.Map;

public interface SendFriendNoticeService {
    public Map<String, String> sendFriendNotice(Integer senderId, Integer receiverId);
}
