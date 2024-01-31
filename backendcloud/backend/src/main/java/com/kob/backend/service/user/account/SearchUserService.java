package com.kob.backend.service.user.account;

import com.alibaba.fastjson.JSONObject;

public interface SearchUserService {
     public JSONObject SearchUser(String username, Integer page);
}
