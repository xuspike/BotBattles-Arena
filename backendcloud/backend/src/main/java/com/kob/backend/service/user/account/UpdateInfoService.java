package com.kob.backend.service.user.account;

import java.util.Map;

public interface UpdateInfoService {
    public Map<String, String> update(Integer userId, String username, String resume, String photo);
}
