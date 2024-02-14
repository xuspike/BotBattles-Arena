package com.kob.backend.service.impl.user.account;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.DynamicNoticeMapper;
import com.kob.backend.mapper.FriendNoticeMapper;
import com.kob.backend.mapper.FriendshipsMapper;
import com.kob.backend.pojo.DynamicNotice;
import com.kob.backend.pojo.FriendNotice;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InfoServiceImpl implements InfoService {
    @Autowired
    private DynamicNoticeMapper dynamicNoticeMapper;
    @Autowired
    private FriendshipsMapper friendshipsMapper;
    @Autowired
    private FriendNoticeMapper friendNoticeMapper;
    @Override
    public JSONObject getinfo() {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();

        JSONObject map = new JSONObject();
        QueryWrapper<DynamicNotice> dynamic_queryWrapper = new QueryWrapper<>();
        dynamic_queryWrapper.eq("user_id", user.getId()).eq("status", 0);
        QueryWrapper<FriendNotice> friend_queryWrapper = new QueryWrapper<>();
        friend_queryWrapper.eq("receiver_id", user.getId()).eq("status", 0);
        map.put("error_message", "success");
        map.put("noticeCount", String.valueOf(dynamicNoticeMapper.selectCount(dynamic_queryWrapper) + friendNoticeMapper.selectCount(friend_queryWrapper)));
        map.put("friendships", friendshipsMapper.findAllFriendsByUserId(user.getId()));
        map.put("id", user.getId().toString());
        map.put("username", user.getUsername());
        map.put("photo", user.getPhoto());
        map.put("resume", user.getResume());
        return map;
    }
}
