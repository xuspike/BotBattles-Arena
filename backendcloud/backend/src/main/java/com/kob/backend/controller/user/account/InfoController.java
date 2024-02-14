package com.kob.backend.controller.user.account;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.user.account.InfoService;
import com.kob.backend.service.user.account.UpdateInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class InfoController {
    @Autowired
    private InfoService infoService;

    @Autowired
    private UpdateInfoService updateInfoService;

    @GetMapping("/api/user/account/info/")
    public JSONObject getinfo() {
        return infoService.getinfo();
    }

    @PostMapping("/api/user/info/update/")
    public Map<String, String> update(@RequestParam Map<String, String> data) {
        Integer userId = Integer.valueOf(data.get("userId"));
        String username = data.get("username");
        String resume = data.get("resume");
        String photo = data.get("photo");

        return updateInfoService.update(userId, username, resume, photo);
    }
}
