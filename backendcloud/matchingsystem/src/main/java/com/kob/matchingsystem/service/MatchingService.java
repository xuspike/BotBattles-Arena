package com.kob.matchingsystem.service;

public interface MatchingService {
    String addPlayer(Integer userId, Integer rating, Integer botId, String mode);
    String removePlayer(Integer userId ,String mode);
}
