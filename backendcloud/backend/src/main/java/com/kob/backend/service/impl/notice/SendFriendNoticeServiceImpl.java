package com.kob.backend.service.impl.notice;

import com.kob.backend.mapper.FriendNoticeMapper;
import com.kob.backend.pojo.FriendNotice;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.notice.SendFriendNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SendFriendNoticeServiceImpl implements SendFriendNoticeService {
    @Autowired
    private FriendNoticeMapper friendNoticeMapper;

    @Override
    public Map<String, String> sendFriendNotice(Integer senderId, Integer receiverId) {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();

        Map<String, String> resp = new HashMap<>();
        if(user.getId() != senderId) {
            resp.put("result", "你没有该权限！");
            return resp;
        }

        friendNoticeMapper.insert(
                new FriendNotice(
                        null,
                        senderId,
                        receiverId,
                        0,
                        new Date()
                )
        );

        resp.put("result", "success");
        return resp;
    }
}
