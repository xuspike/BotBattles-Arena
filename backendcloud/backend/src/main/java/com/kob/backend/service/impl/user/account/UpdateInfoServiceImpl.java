package com.kob.backend.service.impl.user.account;

import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.UpdateInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateInfoServiceImpl implements UpdateInfoService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, String> update(Integer userId, String username, String resume, String photo) {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();

        Map<String, String> resp = new HashMap<>();
        if(userId != user.getId()) {
            resp.put("result", "你没有改权限！");
            return resp;
        }

        if(username == null) {
            resp.put("result", "用户名不能为空！");
            return resp;
        }

        username = username.trim();
        if(username.length() == 0) {
            resp.put("result", "用户名不能为空！");
            return resp;
        }

        if(username.length() > 20) {
            resp.put("result", "用户名长度不能大于20！");
            return resp;
        }

        if(resume.length() > 200) {
            resp.put("result", "个人简介长度不能大于100");
            return resp;
        }

        if(resume == null || resume.length() == 0) {
            resume = "这个用户很懒，什么也没留下~";
        }

        User new_user = new User(
                userId,
                username,
                user.getPassword(),
                photo,
                user.getRating(),
                user.getOpenid(),
                resume,
                user.getDynamicCnt(),
                user.getFriendCnt(),
                user.getLikeCnt()
        );

        userMapper.updateById(new_user);

        resp.put("result", "success");

        return resp;
    }
}
