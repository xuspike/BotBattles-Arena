package com.kob.backend.consumer;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.utils.GravityGame.GravityGame;
import com.kob.backend.consumer.utils.SnakeGame.SnakeGame;
import com.kob.backend.consumer.utils.GobangGame.GobangGame;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.*;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    // ConcurrentHashMap是线程安全的哈希表
    // 让每一个实例访问同一个users（存储目前所有的连接）
    final public static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();
    // 临时匹配池，CopyOnWriteArraySet是线程安全的Set
    private User user; // 当前连接的用户
    private Session session = null; // 每个连接用session来维护
    public SnakeGame snakeGame = null;

    public GobangGame GobangGame = null;

    public GravityGame gravityGame = null;

    private final static String addPlayerUrl = "http://127.0.0.1:3001/player/add/";
    private final static String removePlayerUrl = "http://127.0.0.1:3001/player/remove/";
    public static RestTemplate restTemplate;
    public static UserMapper userMapper;
    public static BotMapper botMapper;
    public static RecordMapper recordMapper;
    // 由于WebSocketServer不是单例的，需要用先定义static静态变量，再用类名接收
    public static GobangRecordMapper gobangRecordMapper;
    public  static GravityRecordMapper gravityRecordMapper;
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }

    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) {
        WebSocketServer.recordMapper = recordMapper;
    }

    @Autowired
    public void setGobangRecordMapper(GobangRecordMapper gobangRecordMapper) {
        WebSocketServer.gobangRecordMapper = gobangRecordMapper;
    }

    @Autowired
    public void setGravityRecordMapper(GravityRecordMapper gravityRecordMapper) {
        WebSocketServer.gravityRecordMapper = gravityRecordMapper;
    }
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        WebSocketServer.restTemplate = restTemplate;
    }

    @Autowired
    public void setBotMapper(BotMapper botMapper) {
        WebSocketServer.botMapper = botMapper;
    }
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session = session;
        // JwtAuthentication是再consumer/utils中封装的类，用token判断用户是否存在
        Integer userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);

        if(this.user != null) {
            users.put(userId, this);
        } else {
            this.session.close();
        }

    }

    @OnClose
    public void onClose() {
        // 关闭连接
        if(this.user != null) {
            users.remove(this.user.getId());
        }
    }

    public static void startSnakeGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        User a = userMapper.selectById(aId), b = userMapper.selectById(bId);
        Bot botA = botMapper.selectById(aBotId), botB = botMapper.selectById(bBotId);

        SnakeGame snakeGame = new SnakeGame(13, 14, 20, a.getId(), botA, b.getId(), botB);
        snakeGame.createMap();

//        由于玩家开始匹配后，在匹配系统会将玩家加入到匹配池中，当玩家关闭游戏后，玩家实际上已经断开连接了
//        但是匹配系统仍然会将两名玩家匹配在一起，并将结果返回给ws端，ws端会调用users.get(xx.getId())
//        从而产生异常
        if(users.get(a.getId()) != null)
            users.get(a.getId()).snakeGame = snakeGame;
        if(users.get(b.getId()) != null)
            users.get(b.getId()).snakeGame = snakeGame;
        snakeGame.start(); // 另起线程执行

        // 将玩家信息和地图封装在一起
        JSONObject respGame = new JSONObject();
        respGame.put("a_id", snakeGame.getPlayerA().getId());
        respGame.put("a_sx", snakeGame.getPlayerA().getSx());
        respGame.put("a_xy", snakeGame.getPlayerA().getSy());
        respGame.put("b_id", snakeGame.getPlayerB().getId());
        respGame.put("b_sx", snakeGame.getPlayerB().getSx());
        respGame.put("b_xy", snakeGame.getPlayerB().getSy());
        respGame.put("map", snakeGame.getG());

        JSONObject respA = new JSONObject();
        respA.put("event", "success-matching");
        respA.put("opponent_username", b.getUsername());
        respA.put("opponent_photo", b.getPhoto());
        respA.put("game", respGame);
        if(users.get(a.getId()) != null)
            users.get(a.getId()).sendMessage(respA.toJSONString());

        JSONObject respB = new JSONObject();
        respB.put("event", "success-matching");
        respB.put("opponent_username", a.getUsername());
        respB.put("opponent_photo", a.getPhoto());
        respB.put("game", respGame);
        if(users.get(b.getId()) != null)
            users.get(b.getId()).sendMessage(respB.toJSONString());
    }

    public static void startGobangGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        User a = userMapper.selectById(aId), b = userMapper.selectById(bId);
        Bot botA = botMapper.selectById(aBotId), botB = botMapper.selectById(bBotId);

        GobangGame GobangGame = new GobangGame(aId, botA, bId, botB);

        if(users.get(a.getId()) != null)
            users.get(a.getId()).GobangGame = GobangGame;
        if(users.get(b.getId()) != null)
            users.get(b.getId()).GobangGame = GobangGame;
        GobangGame.start(); // 另起线程执行

        // 将玩家信息封装在一起
        JSONObject respGame = new JSONObject();
        respGame.put("a_id", GobangGame.getPlayerA().getId());
        respGame.put("b_id", GobangGame.getPlayerB().getId());

        JSONObject respA = new JSONObject();
        respA.put("event", "success-matching");
        respA.put("opponent_username", b.getUsername());
        respA.put("opponent_photo", b.getPhoto());
        respA.put("game", respGame);
        if(users.get(a.getId()) != null)
            users.get(a.getId()).sendMessage(respA.toJSONString());

        JSONObject respB = new JSONObject();
        respB.put("event", "success-matching");
        respB.put("opponent_username", a.getUsername());
        respB.put("opponent_photo", a.getPhoto());
        respB.put("game", respGame);
        if(users.get(b.getId()) != null)
            users.get(b.getId()).sendMessage(respB.toJSONString());
    }

    public static void startGravityGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        User a = userMapper.selectById(aId), b = userMapper.selectById(bId);
        Bot botA = botMapper.selectById(aBotId), botB = botMapper.selectById(bBotId);

         GravityGame gravityGame = new GravityGame(aId, botA, bId, botB);

        if(users.get(a.getId()) != null)
            users.get(a.getId()).gravityGame = gravityGame;
        if(users.get(b.getId()) != null)
            users.get(b.getId()).gravityGame = gravityGame;
        gravityGame.start(); // 另起线程执行

        // 将玩家信息封装在一起
        JSONObject respGame = new JSONObject();
        respGame.put("a_id", gravityGame.getPlayerA().getId());
        respGame.put("b_id", gravityGame.getPlayerB().getId());

        JSONObject respA = new JSONObject();
        respA.put("event", "success-matching");
        respA.put("opponent_username", b.getUsername());
        respA.put("opponent_photo", b.getPhoto());
        respA.put("game", respGame);
        if(users.get(a.getId()) != null)
            users.get(a.getId()).sendMessage(respA.toJSONString());

        JSONObject respB = new JSONObject();
        respB.put("event", "success-matching");
        respB.put("opponent_username", a.getUsername());
        respB.put("opponent_photo", a.getPhoto());
        respB.put("game", respGame);
        if(users.get(b.getId()) != null)
            users.get(b.getId()).sendMessage(respB.toJSONString());
    }

    private void startMatching(Integer botId, String mode) {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        data.add("rating", this.user.getRating().toString());
        data.add("bot_id", botId.toString());
        data.add("mode", mode);
        restTemplate.postForEntity(addPlayerUrl, data, String.class); // String.class是返回值的class
    }

    private void stopMatching(String mode) {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        data.add("mode", mode);
        restTemplate.postForEntity(removePlayerUrl, data, String.class);
    }

    public void move(int direction) {
        if(snakeGame.getPlayerA().getId().equals(user.getId())) {
            if(snakeGame.getPlayerA().getBotId().equals(-1)) //亲自出马
                snakeGame.setNextStepA(direction);
        } else if(snakeGame.getPlayerB().getId().equals(user.getId())) {
            if(snakeGame.getPlayerB().getBotId().equals(-1)) // 亲自出马
                snakeGame.setNextStepB(direction);
        }
    }

    public void drop(int step) {
        if(GobangGame.getPlayerA().getId().equals(user.getId()) && GobangGame.getOperator() == 0) {
            if(GobangGame.getPlayerA().getBotId().equals(-1)) //亲自出马
                GobangGame.setNextStepA(step);
        } else if(GobangGame.getPlayerB().getId().equals(user.getId()) && GobangGame.getOperator() == 1) {
            if(GobangGame.getPlayerB().getBotId().equals(-1)) // 亲自出马
                GobangGame.setNextStepB(step);
        }
    }

    public void fall(int step) {
        if(gravityGame.getPlayerA().getId().equals(user.getId()) && gravityGame.getOperator() == 0) {
            if(gravityGame.getPlayerA().getBotId().equals(-1)) //亲自出马
                gravityGame.setNextStepA(step);
        } else if(gravityGame.getPlayerB().getId().equals(user.getId()) && gravityGame.getOperator() == 1) {
            if(gravityGame.getPlayerB().getBotId().equals(-1)) //亲自出马
                gravityGame.setNextStepB(step);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) { // 一般当做路由，判断把任务交给谁处理
        // 从Client接收消息
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        String mode = data.getString("mode");
        // 反过来调用equals()，可避免event为空时抛出异常
        if("start-matching".equals(event)) {
            startMatching(data.getInteger("bot_id"), mode);
        } else if("stop-matching".equals(event)) {
            stopMatching(mode);
        } else if("move".equals(event)) {
            move(data.getInteger("direction"));
        } else if("drop".equals(event)) {
            drop(data.getInteger("step"));
        } else if("fall".equals(event)) {
            fall(data.getInteger("step"));
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message) {
        // 异步请求要上锁
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}