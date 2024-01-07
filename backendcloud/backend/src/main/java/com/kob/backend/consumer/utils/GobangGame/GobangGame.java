package com.kob.backend.consumer.utils.GobangGame;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.GobangRecord;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class GobangGame extends Thread{
    private final int [][]g;
    private final Player PlayerA, PlayerB;
    // 两名玩家的下一步操作
    private Integer nextStepA = null;
    private Integer nextStepB = null;

    private ReentrantLock lock = new ReentrantLock();

    private String status = "playing"; // playing -> finished

    private Integer operator = 0; // 0表示当前由玩家A操作，1表示当前由玩家B操作
    private String loser = ""; // all：平局，A： A：A输，B：B输

    private final static String addBotUrl = "http://127.0.0.1:3002/bot/add/";

    public GobangGame(Integer idA, Bot botA, Integer idB, Bot botB) {
        this.g = new int[17][17];

        Integer botIdA = -1, botIdB = -1;
        String botCodeA = "", botCodeB = "";
        if(botA != null) {
            botIdA = botA.getId();
            botCodeA = botA.getContent();
        }
        if(botB != null) {
            botIdB = botB.getId();
            botCodeB = botB.getContent();
        }
        PlayerA = new Player(idA, botIdA, botCodeA, new ArrayList<>());
        PlayerB = new Player(idB, botIdB, botCodeB, new ArrayList<>());
    }

    public Player getPlayerA() {
        return PlayerA;
    }

    public Player getPlayerB() {
        return PlayerB;
    }


    // 避免读写冲突，读取操作需要上锁
    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }
    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }
    }

    public int[][] getG() {
        return g;
    }

    private String getInput(Player player) { // 将当前的局面信息编码成字符串
        // 地图#me.sx#me.sy#me.steps#you.sx#you.sy#you.steps
        Player me, you;
        if(PlayerA.getId().equals(player.getId())) {
            me = PlayerA;
            you = PlayerB;
        } else {
            me = PlayerB;
            you = PlayerA;
        }
        return "(" + me.getStepsString() + ")#(" + you.getStepsString() + ")";
    }

    private void sendBotCode(Player player) {
        if(player.getBotId().equals(-1)) return ; // 亲自出马，不需要执行代码
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", player.getId().toString());
        data.add("bot_code", player.getBotCode().toString());
        data.add("input", getInput(player));

        WebSocketServer.restTemplate.postForObject(addBotUrl, data, String.class);
    }

    private boolean nextStep() { // 等待当前操作方的下一步操作
        // 由于前端棋子变大需要约为334ms，后端至少要等334ms让前端渲染完
        try {
            Thread.sleep(334);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(operator == 0) sendBotCode(PlayerA);
        else sendBotCode(PlayerB);

        for(int i = 0; i < 50; i ++) {
            try {
                Thread.sleep(100);
                lock.lock();
                try {
                    if(operator == 0 && nextStepA != null) {
                        if(!judge(nextStepA)) {
                            nextStepA = null;
                        }
                        PlayerA.getSteps().add(nextStepA);
                        operator = 1;
                        return true;
                    }
                    if(operator == 1 && nextStepB != null) {
                        if(!judge(nextStepB)) {
                            nextStepB = null;
                        }
                        PlayerB.getSteps().add(nextStepB);
                        operator = 0;
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        return false;
    }



    private boolean judge(Integer next) { // 判断玩家下一步棋子是否合法
        int x = next / 16 + 1;
        int y = next % 16;

        if(g[x][y] == 0) return true;
        return false;
    }

    private void judge_result() { // 判断游戏输赢

    }

    public void sendAllMessage(String message) {
        if(WebSocketServer.users.get(PlayerA.getId()) != null)
            WebSocketServer.users.get(PlayerA.getId()).sendMessage(message);
        if(WebSocketServer.users.get(PlayerB.getId()) != null)
            WebSocketServer.users.get(PlayerB.getId()).sendMessage(message);
    }

    private void updateUserRating(Player player, Integer rating) {
        User user = WebSocketServer.userMapper.selectById(player.getId());
        user.setRating(rating);
        WebSocketServer.userMapper.updateById(user);
    }

    private void sendDrop() {
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "drop");
            if(operator == 1) { // 在nextStep()中，operator已经被修改
                resp.put("player", "a");
                resp.put("next_step", nextStepA);
                nextStepA = null; // 清空玩家操作
            } else {
                resp.put("player", "b");
                resp.put("next_step", nextStepB);
                nextStepB = null; // 清空玩家操作
            }
            sendAllMessage(resp.toJSONString());
        } finally {
            lock.unlock();
        }
    }

    private void saveToDatabase() { // 将游戏对局存入数据库
        Integer ratingA = WebSocketServer.userMapper.selectById(PlayerA.getId()).getRating();
        Integer ratingB = WebSocketServer.userMapper.selectById(PlayerB.getId()).getRating();

        if("A".equals(loser)) {
            ratingA -= 2;
            ratingB += 5;
        } else {
            ratingA += 5;
            ratingB -= 2;
        }

        updateUserRating(PlayerA, ratingA);
        updateUserRating(PlayerB, ratingB);

        GobangRecord record = new GobangRecord(
                null,
                PlayerA.getId(),
                PlayerB.getId(),
                PlayerA.getStepsString(),
                PlayerB.getStepsString(),
                loser,
                new Date()
        );
        WebSocketServer.gobangRecordMapper.insert(record);
    }

    private void sendResult() { // 向两个client广播结果
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", loser);
        saveToDatabase();
        sendAllMessage(resp.toJSONString());
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            if (nextStep()) { // 是否获取了当前操作方的下一步操作
                judge_result();
                if(status.equals("playing")) {
                    sendDrop();
                } else {
                    sendResult();
                    break;
                }
            } else {
                status = "finished";
                lock.lock();
                try {
                    if (nextStepA == null && nextStepB == null) {
                        loser = "all";
                    } else if (nextStepA == null) {
                        loser = "A";
                    } else if (nextStepB == null) {
                        loser = "B";
                    }
                } finally { // finally保证报异常也会解锁，避免死锁
                    lock.unlock();
                }
                sendResult();
                break;
            }
        }
    }
}
