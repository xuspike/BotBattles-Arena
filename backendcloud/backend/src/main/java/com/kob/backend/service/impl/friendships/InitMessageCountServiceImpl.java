package com.kob.backend.service.impl.friendships;

import com.kob.backend.mapper.FriendshipsMapper;
import com.kob.backend.pojo.Friendships;
import com.kob.backend.service.friendships.InitMessageCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InitMessageCountServiceImpl implements InitMessageCountService {
    @Autowired
    private FriendshipsMapper friendshipsMapper;
    @Override
    public Map<String, String> init(Integer friendshipId, Integer type) {
        Friendships friendship = friendshipsMapper.selectById(friendshipId);
        int cnt1 = friendship.getMsgCnt1(), cnt2 = friendship.getMsgCnt2();
        if(type == 1) cnt1 = 0;
        else cnt2 = 0;
        friendshipsMapper.updateById(
                new Friendships(
                        friendship.getId(),
                        friendship.getUser1Id(),
                        friendship.getUser2Id(),
                        friendship.getLastMsgId(),
                        friendship.getLastTimestamp(),
                        cnt1,
                        cnt2,
                        friendship.getCreatetime()
                )
        );

        Map<String, String> resp = new HashMap<>();
        resp.put("result", "success");
        return resp;
    }
}
