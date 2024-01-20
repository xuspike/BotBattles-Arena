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
        if(userId == -1) queryWrapper.eq("parent_id", -1).orderByDesc("id");
        else queryWrapper.eq("parent_id", -1).eq("user_id", userId).orderByDesc("id");
        List<Dynamic> first_dynamics = dynamicMapper.selectPage(dynamicPage, queryWrapper).getRecords();

        List<JSONObject> items = new LinkedList<>();
        for(Dynamic dynamic : first_dynamics) {
            int first_dynamicId = dynamic.getId();
            queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByAsc("id").eq("parent_id", first_dynamicId);
            List<Dynamic> second_Dynamics = dynamicMapper.selectList(queryWrapper);

            JSONObject item = new JSONObject();
            item.put("parent", dynamic);
            item.put("children", second_Dynamics);
            item.put("childrenCnt", second_Dynamics.size());

            items.add(item);
        }

        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("reply_id", -1);


        JSONObject resp = new JSONObject();
        resp.put("dynamics", items);
        resp.put("parentCnt", dynamicMapper.selectCount(queryWrapper));
        resp.put("result", "success");

        return resp;
    }
}
