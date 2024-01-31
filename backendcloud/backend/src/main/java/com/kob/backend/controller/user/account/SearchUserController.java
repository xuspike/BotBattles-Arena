package com.kob.backend.controller.user.account;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.user.account.SearchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SearchUserController {
    @Autowired
    private SearchUserService searchUserService;

    @GetMapping("/api/user/search/")
    public JSONObject searchUser(@RequestParam Map<String,String> data) {
        String username = data.get("username");
        Integer page = Integer.valueOf(data.get("page"));

        return searchUserService.SearchUser(username, page);
    }
}
