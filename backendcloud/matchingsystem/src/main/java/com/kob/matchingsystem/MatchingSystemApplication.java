package com.kob.matchingsystem;

import com.kob.matchingsystem.service.impl.MatchingServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MatchingSystemApplication {
    public static void main(String[] args) {
        MatchingServiceImpl.SNAKE_MATCHING_POOL.start(); // 启动贪吃蛇匹配线程
        MatchingServiceImpl.GOBANG_MATCHING_POOL.start(); // 启动五子棋匹配线程
        MatchingServiceImpl.GRAVITY_MATCHING_POOL.start(); // 启动重力五子棋匹配线程
        SpringApplication.run(MatchingSystemApplication.class, args);
    }
}