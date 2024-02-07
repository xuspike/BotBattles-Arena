package com.kob.backend.service.friendships;

import java.util.Map;

public interface InitMessageCountService {
    public Map<String, String> init(Integer friendshipId, Integer type); // type为1是用户1， type为2使用户2
}
