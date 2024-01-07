package com.kob.matchingsystem.service.impl;

import com.kob.matchingsystem.service.MatchingService;
import com.kob.matchingsystem.service.impl.utils.GobangMatchingPool;
import com.kob.matchingsystem.service.impl.utils.SnakeMatchingPool;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceImpl implements MatchingService {
    public final static SnakeMatchingPool SNAKE_MATCHING_POOL = new SnakeMatchingPool();
    public final static GobangMatchingPool GOBANG_MATCHING_POOL = new GobangMatchingPool();

    @Override
    public String addPlayer(Integer userId, Integer rating, Integer botId, String mode) {
        if(mode.equals("snake")) {
            SNAKE_MATCHING_POOL.addPlayer(userId, rating, botId, mode);
        } else if(mode.equals("gobang")) {
            GOBANG_MATCHING_POOL.addPlayer(userId, rating, botId, mode);
        }

        return "add player success";
    }

    @Override
    public String removePlayer(Integer userId, String mode) {
        if(mode.equals("snake")) {
            SNAKE_MATCHING_POOL.removePlayer(userId);
        } else if(mode.equals("gobang")) {
            GOBANG_MATCHING_POOL.removePlayer(userId);
        }

        return "remove player success";
    }
}
