package com.kob.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Friendships {
    @TableId(type = IdType.AUTO) // 按id自增
    private Integer id;
    private Integer user1Id;
    private Integer user2Id;
    private Integer lastMsgId;
    private BigInteger lastTimestamp;
    private Integer msgCnt1; // 用户1未读消息数
    private Integer msgCnt2; // 用户2未读消息数
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai") // 定义日期格式
    private Date createtime;
}
