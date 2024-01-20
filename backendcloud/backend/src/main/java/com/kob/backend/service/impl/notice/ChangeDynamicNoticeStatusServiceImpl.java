package com.kob.backend.service.impl.notice;

import com.kob.backend.mapper.DynamicNoticeMapper;
import com.kob.backend.pojo.DynamicNotice;
import com.kob.backend.service.notice.ChangeDynamicNoticeStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChangeDynamicNoticeStatusServiceImpl implements ChangeDynamicNoticeStatusService {
    @Autowired
    private DynamicNoticeMapper dynamicNoticeMapper;

    @Override
    public Map<String, String> changeStatus(String noticeIds) {
        String[] ids = noticeIds.split("#");

        Map<String, String> resp = new HashMap<>();
        if(ids.length <= 0) {
            resp.put("result", "操作不当！");
            return resp;
        }

        for(int i = 0; i < ids.length; i ++) {
            int id = Integer.parseInt(ids[i]);

            DynamicNotice dynamicNotice = dynamicNoticeMapper.selectById(id);
            dynamicNoticeMapper.updateById(
                    new DynamicNotice(
                            dynamicNotice.getId(),
                            dynamicNotice.getUserId(),
                            dynamicNotice.getSenderId(),
                            dynamicNotice.getSenderDynamicId(),
                            dynamicNotice.getDynamicId(),
                            1 - dynamicNotice.getStatus(),
                            dynamicNotice.getCreatetime()
                    )
            );
        }

        resp.put("result", "success");
        return resp;
    }
}
