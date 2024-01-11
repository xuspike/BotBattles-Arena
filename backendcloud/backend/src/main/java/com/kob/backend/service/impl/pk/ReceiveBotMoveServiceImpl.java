package com.kob.backend.service.impl.pk;

import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.consumer.utils.GobangGame.GobangGame;
import com.kob.backend.consumer.utils.SnakeGame.SnakeGame;
import com.kob.backend.service.pk.ReceiveBotMoveService;
import org.springframework.stereotype.Service;

@Service
public class ReceiveBotMoveServiceImpl implements ReceiveBotMoveService {
    @Override
    public String receiveBotMove(Integer userId, Integer operate, String mode) {
        if(WebSocketServer.users.get(userId) != null) {
            if("snake".equals(mode)) {
                SnakeGame snakeGame = WebSocketServer.users.get(userId).snakeGame;
                if(snakeGame != null) {
                    if(snakeGame.getPlayerA().getId().equals(userId)) {
                        snakeGame.setNextStepA(operate);
                    } else if(snakeGame.getPlayerB().getId().equals(userId)) {
                        snakeGame.setNextStepB(operate);
                    }
                }
            } else if("gobang".equals(mode)) {
                GobangGame gobangGame = WebSocketServer.users.get(userId).GobangGame;
                if(gobangGame != null) {
                    if(gobangGame.getPlayerA().getId().equals(userId)) {
                        gobangGame.setNextStepA(operate);
                    } else if(gobangGame.getPlayerB().getId().equals(userId)) {
                        gobangGame.setNextStepB(operate);
                    }
                }
            }
        }
        return "receive bot move success";
    }
}
