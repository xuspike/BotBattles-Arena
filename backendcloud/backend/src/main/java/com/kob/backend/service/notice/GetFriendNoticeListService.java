package com.kob.backend.service.notice;

import com.alibaba.fastjson.JSONObject;

public interface GetFriendNoticeListService {
    public JSONObject getList(Integer userId, Integer page);
}
