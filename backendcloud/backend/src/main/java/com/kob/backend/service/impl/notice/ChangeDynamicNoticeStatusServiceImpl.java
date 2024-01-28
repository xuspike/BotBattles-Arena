package com.kob.backend.service.impl.notice;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.DynamicNoticeMapper;
import com.kob.backend.pojo.DynamicNotice;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.notice.ChangeDynamicNoticeStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChangeDynamicNoticeStatusServiceImpl implements ChangeDynamicNoticeStatusService {
    @Autowired
    private DynamicNoticeMapper dynamicNoticeMapper;

    @Override
    public Map<String, String> changeStatus(Integer noticeId) {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();

        Map<String, String> resp = new HashMap<>();

        if(noticeId == -1) {
            QueryWrapper<DynamicNotice> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", user.getId()).eq("status", 0);
            List<DynamicNotice> dynamicNotices = dynamicNoticeMapper.selectList(queryWrapper);
            for(DynamicNotice dynamicNotice : dynamicNotices) {
                dynamicNoticeMapper.updateById(
                        new DynamicNotice(
                                dynamicNotice.getId(),
                                dynamicNotice.getUserId(),
                                dynamicNotice.getSenderId(),
                                dynamicNotice.getSenderDynamicId(),
                                dynamicNotice.getDynamicId(),
                                1,
                                dynamicNotice.getCreatetime()
                        )
                );
            }
        } else {
            DynamicNotice dynamicNotice = dynamicNoticeMapper.selectById(noticeId);
            dynamicNoticeMapper.updateById(
                    new DynamicNotice(
                            dynamicNotice.getId(),
                            dynamicNotice.getUserId(),
                            dynamicNotice.getSenderId(),
                            dynamicNotice.getSenderDynamicId(),
                            dynamicNotice.getDynamicId(),
                            1,
                            dynamicNotice.getCreatetime()
                    )
            );
        }

        resp.put("result", "success");
        return resp;
    }
}
