package com.kob.backend.service.dynamic;


import com.alibaba.fastjson.JSONObject;

public interface GetDynamicListService {
    public JSONObject getList(Integer userId, Integer page);
}
