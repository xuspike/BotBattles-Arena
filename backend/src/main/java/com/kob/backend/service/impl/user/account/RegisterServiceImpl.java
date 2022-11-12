package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype   .Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> register(String username, String password, String confirm_password) {
        Map<String, String> map = new HashMap<>();
        if(username == null) {
            map.put("error_message", "用户名不能为空");
            return map;
        }
        if(password == null || confirm_password == null) {
            map.put("error_message", "密码不能为空");
            return map;
        }

        username = username.trim();
        if(username.length() == 0) {
            map.put("error_message", "用户名不能为空");
            return map;
        }

        if(password.length() == 0 || confirm_password.length() == 0) {
            map.put("error_message", "密码不能为空");
            return map;
        }

        if(username.length() > 100) {
            map.put("error_message", "用户名长度不能大于100");
            return map;
        }

        if(password.length() > 100 || confirm_password.length() > 100) {
            map.put("error_message", "密码长度不能大于100");
            return map;
        }

        if(!password.equals(confirm_password)) {
            map.put("error_message", "两次输入的密码不一致");
            return map;
        }

        // 查询数据库当中是否有username的用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("username", username);
        List<User> users = userMapper.selectList(queryWrapper);
        if(!users.isEmpty()) {
            map.put("error_message", "用户名已存在");
            return map;
        }

        // 加密
        String encodePassword = passwordEncoder.encode(password);
        String photo = "https://cdn.acwing.com/media/user/profile/photo/135321_lg_d068f5378c.jpg";
        // id已经配置过自增，会自动生成
        User user = new User(null, username, encodePassword, photo);
        userMapper.insert(user);

        map.put("error_message", "success");
        return map;
    }
}
