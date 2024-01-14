package com.kob.backend.service.impl.pk;

import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.service.pk.StartGameService;
import org.springframework.stereotype.Service;

@Service
public class StartGameServiceImpl implements StartGameService {
    @Override
    public String startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId, String mode) {
        System.out.println("start game " + aId + " " + bId + " " + mode);
        if("snake".equals(mode)) {
            WebSocketServer.startSnakeGame(aId, aBotId, bId, bBotId);
        } else if("gobang".equals(mode)) {
            WebSocketServer.startGobangGame(aId, aBotId, bId, bBotId);
        } else if("gravity".equals(mode)) {
            WebSocketServer.startGravityGame(aId, aBotId, bId, bBotId);
        }

        return "start game success";
    }
}
