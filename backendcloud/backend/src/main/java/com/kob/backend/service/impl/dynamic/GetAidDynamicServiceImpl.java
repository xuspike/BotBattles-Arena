package com.kob.backend.service.impl.dynamic;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.DynamicMapper;
import com.kob.backend.pojo.Dynamic;
import com.kob.backend.service.dynamic.GetAidDynamicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAidDynamicServiceImpl implements GetAidDynamicService {
    @Autowired
    private DynamicMapper dynamicMapper;

    @Override
    public JSONObject getAid(Integer dynamicId) {
        Dynamic dynamic = dynamicMapper.selectById(dynamicId);

        JSONObject resp = new JSONObject();
        if(dynamic == null) {
            resp.put("result", "操作不当！");
            return resp;
        }
        if(dynamic.getParentId() != -1) {
            dynamic = dynamicMapper.selectById(dynamic.getParentId());
        }

        QueryWrapper<Dynamic> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", dynamicId);
        List<Dynamic> children_dynamic = dynamicMapper.selectList(queryWrapper);

        resp.put("childrenCnt", children_dynamic.size());
        resp.put("parent", dynamic);
        resp.put("children", children_dynamic);
        resp.put("result", "success");

        return resp;
    }
}
