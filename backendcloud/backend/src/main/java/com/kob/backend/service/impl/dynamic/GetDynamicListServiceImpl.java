package com.kob.backend.service.impl.dynamic;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.DynamicMapper;
import com.kob.backend.pojo.Dynamic;
import com.kob.backend.service.dynamic.GetDynamicListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class GetDynamicListServiceImpl implements GetDynamicListService {
    @Autowired
    private DynamicMapper dynamicMapper;

    @Override
    public JSONObject getList(Integer userId, Integer page) {
        IPage<Dynamic> dynamicPage = new Page<>(page, 5);

        QueryWrapper<Dynamic> queryWrapper = new QueryWrapper<>();
        if(userId != -1) queryWrapper.orderByDesc("id");
        else queryWrapper.eq("user_id", userId).orderByDesc("id");
        List<Dynamic> first_dynamics = dynamicMapper.selectPage(dynamicPage, queryWrapper).getRecords();

        List<JSONObject> items = new LinkedList<>();
        for(Dynamic dynamic : first_dynamics) {
            int first_dynamicId = dynamic.getId();
            queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("id").eq("parent_id", first_dynamicId);
            List<Dynamic> second_Dynamics = dynamicMapper.selectList(queryWrapper);

            JSONObject item = new JSONObject();
            item.put("parent", dynamic);
            item.put("children", second_Dynamics);

            items.add(item);
        }

        JSONObject resp = new JSONObject();
        resp.put("dynamics", items);
        resp.put("result", "success");

        return resp;
    }
}
