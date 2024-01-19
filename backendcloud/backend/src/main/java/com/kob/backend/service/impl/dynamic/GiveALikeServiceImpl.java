package com.kob.backend.service.impl.dynamic;

import com.kob.backend.mapper.DynamicMapper;
import com.kob.backend.pojo.Dynamic;
import com.kob.backend.service.dynamic.GiveALikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GiveALikeServiceImpl implements GiveALikeService {
    @Autowired
    private DynamicMapper dynamicMapper;

    @Override
    public Map<String, String> giveLike(Integer dynamicId, Integer num) {
        Dynamic dynamic = dynamicMapper.selectById(dynamicId);

        Map<String, String> resp = new HashMap<>();
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

        dynamicMapper.updateById(new_dynamic);
        resp.put("result", "success");

        return resp;
    }
}
