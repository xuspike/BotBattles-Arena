package com.kob.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DynamicNotice {
    @TableId(type = IdType.AUTO) // 按id自增
    private Integer id;
    private Integer userId;
    private Integer senderId;
    private Integer senderDynamicId;
    private Integer dynamicId;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai") // 定义日期格式
    private Date createtime;
}
