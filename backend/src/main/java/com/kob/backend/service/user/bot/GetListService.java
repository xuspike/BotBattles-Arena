package com.kob.backend.service.user.bot;

import com.kob.backend.pojo.Bot;

import java.util.List;

public interface GetListService {
    List<Bot> getlist(); // 返回自己的信息，用的token，不需要传参
}
