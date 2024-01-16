package com.kob.backend.service.dynamic;

import java.util.Map;

public interface CreateDynamicService {
    public Map<String, String> create(Integer userId, Integer replyId, String content, String photos);
}
