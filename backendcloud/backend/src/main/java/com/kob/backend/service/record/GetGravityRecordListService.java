package com.kob.backend.service.record;

import com.alibaba.fastjson.JSONObject;

public interface GetGravityRecordListService {
    JSONObject getList(Integer page);
}
