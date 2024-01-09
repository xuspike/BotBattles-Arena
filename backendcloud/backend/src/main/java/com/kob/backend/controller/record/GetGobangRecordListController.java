package com.kob.backend.controller.record;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.record.GetGobangRecoredListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetGobangRecordListController {
    @Autowired
    private GetGobangRecoredListService getGobangRecoredListService;

    @GetMapping("/api/gobang_record/getlist/")
    JSONObject getlist(@RequestParam Map<String, String> data) {
        Integer page = Integer.parseInt(data.get("page"));
        return getGobangRecoredListService.getList(page);
    }
}
