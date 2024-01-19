package com.kob.backend.service.impl.dynamic;

import com.kob.backend.mapper.DynamicMapper;
import com.kob.backend.pojo.Dynamic;
import com.kob.backend.pojo.User;
import com.kob.backend.service.dynamic.CreateDynamicService;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CreateDynamicServiceImpl implements CreateDynamicService {
    @Autowired
    private DynamicMapper dynamicMapper;

    @Override
    public Map<String, String> create(Integer userId, Integer replyId, String content, String photos) {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();
        Map<String, String> resp = new HashMap<>();
        if(user.getId() != userId) resp.put("result", "failure");

        Integer parentId = -1;
        String replyName = "";

        Dynamic reply_dynamic = dynamicMapper.selectById(replyId);
        if(replyId != -1) {
            parentId = replyId;
            if(reply_dynamic.getParentId() != -1) {
                parentId = reply_dynamic.getParentId();
            }
        }
        if(reply_dynamic != null) {
            replyName = reply_dynamic.getUsername();
        }

        Dynamic dynamic = new Dynamic(null, userId, user.getUsername(), user.getPhoto(), parentId, replyId, replyName, content, photos, 0, new Date());
        dynamicMapper.insert(dynamic);

        resp.put("result", "success");

        return resp;
    }
}
