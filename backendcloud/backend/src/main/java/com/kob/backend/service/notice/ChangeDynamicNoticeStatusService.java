package com.kob.backend.service.notice;

import java.util.Map;

public interface ChangeDynamicNoticeStatusService {
    public Map<String, String> changeStatus(Integer noticeIds);
}
