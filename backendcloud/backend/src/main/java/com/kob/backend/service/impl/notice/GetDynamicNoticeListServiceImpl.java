package com.kob.backend.service.impl.notice;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.DynamicMapper;
import com.kob.backend.mapper.DynamicNoticeMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Dynamic;
import com.kob.backend.pojo.DynamicNotice;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.notice.GetDynamicNoticeListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetDynamicNoticeListServiceImpl implements GetDynamicNoticeListService {
    @Autowired
    private DynamicNoticeMapper dynamicNoticeMapper;
    @Autowired
    private DynamicMapper dynamicMapper;
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

        IPage<DynamicNotice> noticePage = new Page<>(page, 10);

        QueryWrapper<DynamicNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).orderBy(true, true, "status").orderBy(true, false, "id");
        List<DynamicNotice> dynamicNotices = dynamicNoticeMapper.selectPage(noticePage, queryWrapper).getRecords();
        List<JSONObject> items = new ArrayList<>();
        for(DynamicNotice dynamicNotice : dynamicNotices) {
            JSONObject item = new JSONObject();
            if(dynamicNotice.getSenderDynamicId() != -1) {
                item.put("senderDynamic", dynamicMapper.selectById(dynamicNotice.getSenderDynamicId()));
            }
            else {
                User sender = userMapper.selectById(dynamicNotice.getSenderId());
                item.put("senderUser", sender);
            }
            item.put("userDynamic", dynamicMapper.selectById(dynamicNotice.getDynamicId()));
            item.put("notice", dynamicNotice);
            items.add(item);
        }

        resp.put("noticeCount", dynamicNoticeMapper.selectCount(queryWrapper));
        resp.put("dynamicNotices", items);
        resp.put("result", "success");

        return resp;
    }
}
