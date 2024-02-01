package com.kob.backend.service.impl.notice;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.FriendNoticeMapper;
import com.kob.backend.mapper.FriendshipsMapper;
import com.kob.backend.pojo.FriendNotice;
import com.kob.backend.pojo.Friendships;
import com.kob.backend.service.notice.ChangeFriendNoticeStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ChangeFriendNoticeStatusServiceImpl implements ChangeFriendNoticeStatusService {
    @Autowired
    private FriendNoticeMapper friendNoticeMapper;

    @Autowired
    private FriendshipsMapper friendshipsMapper;

    @Override
    public Map<String, String> changeStatus(Integer noticeId, Integer operation) {
        Map<String, String> resp = new HashMap<>();
        if(operation != 1 && operation != 2) {
            resp.put("result", "操作不当！");
            return resp;
        }

        FriendNotice friendNotice = friendNoticeMapper.selectById(noticeId);

        Integer senderId = friendNotice.getSenderId();
        Integer receiverId = friendNotice.getReceiverId();

        QueryWrapper<Friendships> queryWrapper = new QueryWrapper<>();
        queryWrapper.nested(i -> i.eq("user1_id", senderId).eq("user2_id", receiverId)).or()
                .nested(i -> i.eq("user2_id", senderId).eq("user1_id", receiverId));
        Friendships friendship = friendshipsMapper.selectOne(queryWrapper);

        // 如果说在这个请求前已经有个请求被同意了（两人已经是好友了）
        if(friendship != null) {
            friendNoticeMapper.updateById(
                    new FriendNotice(
                            friendNotice.getId(),
                            friendNotice.getSenderId(),
                            friendNotice.getReceiverId(),
                            1,
                            friendNotice.getCreatetime()
                    )
            );
            resp.put("result", "你们已经是好友啦！");
            return resp;
        }

        if(operation == 1) {
            friendshipsMapper.insert(
                    new Friendships(
                            null,
                            senderId,
                            receiverId,
                            -1,
                            null,
                            0,
                            0,
                            new Date()
                    )
            );
        }

        friendNoticeMapper.updateById(
                new FriendNotice(
                        friendNotice.getId(),
                        friendNotice.getSenderId(),
                        friendNotice.getReceiverId(),
                        operation,
                        friendNotice.getCreatetime()
                )
        );

        resp.put("result", "success");

        return resp;
    }
}
