package com.kob.backend.service.notice;

import java.util.Map;

public interface ChangeFriendNoticeStatusService {
    public Map<String, String> changeStatus(Integer noticeId, Integer operation);
}
