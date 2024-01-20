package com.kob.backend.service.friendships;

import java.util.Map;

public interface CreateFriendshipsService {
    public Map<String, String> create(Integer user1Id, Integer user2Id);
}
