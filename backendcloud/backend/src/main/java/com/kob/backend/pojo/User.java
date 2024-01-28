package com.kob.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 生成get、set、hashCode、equals等方法，提高代码简洁
@NoArgsConstructor // 生成无参构造函数
@AllArgsConstructor // 生成有参构造函数
public class User {
    @TableId(type = IdType.AUTO) // 按id自增
    private Integer id;
    private String username;
    private String password;
    private String photo;
    private Integer rating;
    private String openid;
    private String resume;
}
