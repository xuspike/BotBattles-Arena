package com.kob.backend.service.impl.user.account;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.account.SearchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchUserServiceImpl implements SearchUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject SearchUser(String username, Integer page) {
        JSONObject resp = new JSONObject();

        IPage<User> userIPage = new Page<>(page, 9);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", username);

        List<User> users = userMapper.selectPage(userIPage, queryWrapper).getRecords();

        resp.put("usersCount", userMapper.selectCount(queryWrapper));
        resp.put("users", users);
        resp.put("result", "success");

        return resp;
    }
}
