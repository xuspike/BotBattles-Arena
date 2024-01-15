package com.kob.backend.controller.pk;

import com.kob.backend.service.pk.ReceiveBotMoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class ReceiveBotMoveController {
    @Autowired
    private ReceiveBotMoveService receiveBotMoveService;

    @PostMapping("/pk/receive/bot/move/")
    public String receiveBotMove(@RequestParam MultiValueMap<String, String> data) {
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        String mode = data.getFirst("mode");
        Integer operate = -1;
        System.out.print("data = ");
        System.out.println(data);
        if("snake".equals(mode)) {
            operate = Integer.parseInt(Objects.requireNonNull(data.getFirst("direction")));
            System.out.print("snake_operate = ");
            System.out.println(operate);
        }
        else if("gobang".equals(mode)) {
            operate = Integer.parseInt(Objects.requireNonNull(data.getFirst("step")));
            System.out.print("gobang_operate = ");
            System.out.println(operate);
        } else if("gravity".equals(mode)) {
            operate = Integer.parseInt(Objects.requireNonNull(data.getFirst("step")));
            System.out.print("gobang_operate = ");
            System.out.println(operate);
        }
        return receiveBotMoveService.receiveBotMove(userId, operate, mode);
    }
}
