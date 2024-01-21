package com.kob.backend.service.impl.notice;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.DynamicNoticeMapper;
import com.kob.backend.pojo.DynamicNotice;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.notice.GetDynamicNoticeListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetDynamicNoticeListServiceImpl implements GetDynamicNoticeListService {
    @Autowired
    private DynamicNoticeMapper dynamicNoticeMapper;

    @Override
    public JSONObject getList(Integer userId, Integer status) {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();
        JSONObject resp = new JSONObject();

        if(status != 0 && status != 1) {
            resp.put("result", "操作不当！");
            return resp;
        }

        if(user.getId() != userId) {
            resp.put("result", "你没有该权限！");
            return resp;
        }

        QueryWrapper<DynamicNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("status", status).orderByDesc("id");
        List<DynamicNotice> dynamicNotices = dynamicNoticeMapper.selectList(queryWrapper);

        resp.put("dynamicNotices", dynamicNotices);
        resp.put("result", "success");

        return resp;
    }
}
