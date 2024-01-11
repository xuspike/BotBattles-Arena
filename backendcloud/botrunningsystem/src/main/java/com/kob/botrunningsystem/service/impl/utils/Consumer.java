package com.kob.botrunningsystem.service.impl.utils;

import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.UUID;
import java.util.function.Supplier;

@Component
public class Consumer extends Thread{
    private Bot bot;
    private static RestTemplate restTemplate;

    private final static String receiveBotMoveUrl = "http://127.0.0.1:3000/pk/receive/bot/move/";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        Consumer.restTemplate = restTemplate;
    }

    public void startTimeout(long timeout, Bot bot) {
        this.bot = bot;
        this.start();

        try {
            this.join(timeout); // 最多等待timeout秒，超时后执行后面代码
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.interrupt(); // 中断当前线程
        }
    }

    private String addUid(String code, String uid) { // 在code中的Bot类名后添加uid
        int k = code.indexOf(" implements java.util.function.Supplier<Integer>");
        return code.substring(0, k) + uid + code.substring(k);
    }

    // 编译代码
    @Override
    public void run() {
        UUID uuid = UUID.randomUUID(); // 随机生成一段字符串
        String uid = uuid.toString().substring(0, 8);
        // Supplier是java自带的接口,Reflect可以动态编译一段字符串代码
        Supplier<Integer> botInterface = Reflect.compile(
            "com.kob.botrunningsystem.utils.Bot" + uid,
                addUid(bot.getBotCode(), uid)
        ).create().get();

        File file = new File("input.txt");

        try(PrintWriter fout = new PrintWriter(file)) {
            fout.println(bot.getInput());
            fout.flush(); // 不清缓冲区的话，文件里可能读不到信息
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Integer res = botInterface.get(); // Supplier含有get函数，返回编译结果

        System.out.println("res: " + bot.getUserId() + " " + res);
        System.out.print("mode = ");
        System.out.println(bot.getMode());

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", bot.getUserId().toString());
        if("snake".equals(bot.getMode())) {
            data.add("direction", res.toString());
            data.add("mode", "snake");
        }
        else if("gobang".equals(bot.getMode()))  {
            data.add("step", res.toString());
            data.add("mode", "gobang");
        }
        restTemplate.postForObject(receiveBotMoveUrl, data, String.class);
    }
}
