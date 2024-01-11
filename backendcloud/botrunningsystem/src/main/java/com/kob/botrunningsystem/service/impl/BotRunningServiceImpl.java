package com.kob.botrunningsystem.service.impl;

import com.kob.botrunningsystem.service.BotRunningService;
import com.kob.botrunningsystem.service.impl.utils.BotPool;
import org.springframework.stereotype.Service;

@Service
public class BotRunningServiceImpl implements BotRunningService {
    public final static BotPool botpool = new BotPool();
    @Override
    public String addBot(Integer userId, String botCode, String input, String mode) {
        // System.out.println("add bot: " + userId + " " + botCode + " " + input + " " + mode);
        botpool.addBot(userId, botCode, input, mode);
        return "add bot success";
    }
}
