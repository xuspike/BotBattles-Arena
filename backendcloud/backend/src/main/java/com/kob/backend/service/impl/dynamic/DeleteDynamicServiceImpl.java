package com.kob.backend.service.impl.dynamic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.DynamicMapper;
import com.kob.backend.pojo.Dynamic;
import com.kob.backend.pojo.User;
import com.kob.backend.service.dynamic.DeleteDynamicService;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DeleteDynamicServiceImpl implements DeleteDynamicService {
    @Autowired
    private DynamicMapper dynamicMapper;
    @Override
    public Map<String, String> delete(Integer dynamicId) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        Dynamic dynamic = dynamicMapper.selectById(dynamicId);
        int userId = user.getId();

        Map<String, String> resp = new HashMap<>();

        if(dynamicId == null) {
            resp.put("result", "该动态不存在或已被删除！");
            return resp;
        }

        if(user.getId() != userId) {
            resp.put("result", "你没有权限删除此动态！");
            return resp;
        }

        QueryWrapper<Dynamic> queryWrapper = new QueryWrapper<>();
        if(dynamic.getReplyId() == -1) {
            queryWrapper.eq("parent_id", dynamicId);
            dynamicMapper.delete(queryWrapper);
        } else {
            queryWrapper.eq("reply_id", dynamicId);
            dynamicMapper.delete(queryWrapper);
        }

        dynamicMapper.deleteById(dynamicId);

        resp.put("result", "success");
        return resp;
    }
}
