package com.kob.backend.service.impl.pk;

import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.consumer.utils.SnakeGame.SnakeGame;
import com.kob.backend.service.pk.ReceiveBotMoveService;
import org.springframework.stereotype.Service;

@Service
public class ReceiveBotMoveServiceImpl implements ReceiveBotMoveService {
    @Override
    public String receiveBotMove(Integer userId, Integer direction) {
        if(WebSocketServer.users.get(userId) != null) {
            SnakeGame snakeGame = WebSocketServer.users.get(userId).snakeGame;
            if(snakeGame != null) {
                if(snakeGame.getPlayerA().getId().equals(userId)) {
                    snakeGame.setNextStepA(direction);
                } else if(snakeGame.getPlayerB().getId().equals(userId)) {
                    snakeGame.setNextStepB(direction);
                }
            }
        }
        return "receive bot move success";
    }
}
