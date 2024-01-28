package com.kob.backend.service.impl.dynamic;

import com.kob.backend.mapper.DynamicMapper;
import com.kob.backend.mapper.DynamicNoticeMapper;
import com.kob.backend.pojo.Dynamic;
import com.kob.backend.pojo.DynamicNotice;
import com.kob.backend.pojo.User;
import com.kob.backend.service.dynamic.GiveALikeService;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class GiveALikeServiceImpl implements GiveALikeService {
    @Autowired
    private DynamicMapper dynamicMapper;
    @Autowired
    private DynamicNoticeMapper dynamicNoticeMapper;

    @Override
    public Map<String, String> giveLike(Integer dynamicId, Integer num) {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();

        Dynamic dynamic = dynamicMapper.selectById(dynamicId);

        Map<String, String> resp = new HashMap<>();
        if(dynamic == null) {
            resp.put("result", "操作不当！");
            return resp;
        }
        if(num != -1 && num != 1) {
            resp.put("result", "操作不当！");
            return resp;
        }

        Dynamic new_dynamic = new Dynamic(
                dynamic.getId(),
                dynamic.getUserId(),
                dynamic.getUsername(),
                dynamic.getUserPhoto(),
                dynamic.getParentId(),
                dynamic.getReplyId(),
                dynamic.getReplyName(),
                dynamic.getContent(),
                dynamic.getPhotos(),
                dynamic.getLikes() + num,
                dynamic.getCreatetime()
        );

        if(num == 1) {
            dynamicNoticeMapper.insert(new DynamicNotice(
                    null,
                    new_dynamic.getUserId(),
                    user.getId(),
                    -1, // 点赞时为-1
                    dynamicId,
                    0,
                    new Date()
            ));
        }

        dynamicMapper.updateById(new_dynamic);
        resp.put("result", "success");

        return resp;
    }
}
