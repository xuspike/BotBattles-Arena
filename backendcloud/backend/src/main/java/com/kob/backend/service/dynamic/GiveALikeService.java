package com.kob.backend.service.dynamic;

import java.util.Map;

public interface GiveALikeService {
    public Map<String, String> giveLike(Integer dynamicId, Integer num);
}
