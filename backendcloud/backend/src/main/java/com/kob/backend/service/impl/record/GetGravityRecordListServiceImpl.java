package com.kob.backend.service.impl.record;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.GravityRecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.GravityRecord;
import com.kob.backend.pojo.User;
import com.kob.backend.service.record.GetGravityRecordListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class GetGravityRecordListServiceImpl implements GetGravityRecordListService {
    @Autowired
    private GravityRecordMapper gravityRecordMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getList(Integer page) {
        IPage<GravityRecord> recordIPage = new Page<>(page, 10);
        QueryWrapper<GravityRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id"); // id是递增的，可以保证有序
        List<GravityRecord> records = gravityRecordMapper.selectPage(recordIPage, queryWrapper).getRecords();
        JSONObject resp = new JSONObject();
        List<JSONObject> items = new LinkedList<>();
        for(GravityRecord record: records) {
            User userA = userMapper.selectById(record.getAId());
            User userB = userMapper.selectById(record.getBId());
            JSONObject item = new JSONObject();
            item.put("a_photo", userA.getPhoto());
            item.put("a_username", userA.getUsername());
            item.put("b_photo", userB.getPhoto());
            item.put("b_username", userB.getUsername());
            String result = "平局";
            if("A".equals(record.getLoser())) result = "B胜";
            else if("B".equals((record.getLoser()))) result = "A胜";
            item.put("result", result);
            item.put("record", record);
            items.add(item);
        }
        resp.put("records", items);
        resp.put("records_count", gravityRecordMapper.selectCount(null)); // null表示无条件返回

        return resp;
    }
}
