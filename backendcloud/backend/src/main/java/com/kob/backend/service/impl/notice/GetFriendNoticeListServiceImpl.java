package com.kob.backend.service.impl.notice;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.FriendNoticeMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.FriendNotice;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.notice.GetFriendNoticeListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetFriendNoticeListServiceImpl implements GetFriendNoticeListService {
    @Autowired
    private FriendNoticeMapper friendNoticeMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getList(Integer userId, Integer page) {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();
        JSONObject resp = new JSONObject();


        if(user.getId() != userId) {
            resp.put("result", "你没有该权限！");
            return resp;
        }

        IPage<FriendNotice> noticePage = new Page<>(page, 10);

        QueryWrapper<FriendNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receiver_id", userId).orderBy(true, true, "status").orderBy(true, false, "id");
        List<FriendNotice> friendNotices = friendNoticeMapper.selectPage(noticePage, queryWrapper).getRecords();
        List<JSONObject> items = new ArrayList<>();
        for(FriendNotice friendNotice : friendNotices) {
            JSONObject item = new JSONObject();
            User sender = userMapper.selectById(friendNotice.getSenderId());
            item.put("senderUser", sender);
            item.put("notice", friendNotice);
            items.add(item);
        }

        resp.put("noticeCount", friendNoticeMapper.selectCount(queryWrapper));
        resp.put("friendNotices", items);
        resp.put("result", "success");

        return resp;
    }
}
