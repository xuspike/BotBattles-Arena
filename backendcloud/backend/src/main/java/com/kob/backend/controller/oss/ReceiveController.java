package com.kob.backend.controller.oss;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ali/oss")
public class ReceiveController {
    @PostMapping("/receive/")
    public Map<String, String> ReceiveUrl(@RequestParam Map<String, String> data) {
        Map<String, String> resp = new HashMap<>();
        resp.put("result", "success");
        resp.put("imgName", data.get("filename"));
        resp.put("imgUrl", "https://xrookie.oss-cn-hangzhou.aliyuncs.com/" + data.get("randomname"));
        return resp;
    }
}
