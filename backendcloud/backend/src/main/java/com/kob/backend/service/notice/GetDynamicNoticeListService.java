package com.kob.backend.service.notice;

import com.alibaba.fastjson.JSONObject;

public interface GetDynamicNoticeListService {
    public JSONObject getList(Integer userId, Integer status);
}
