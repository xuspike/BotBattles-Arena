package com.kob.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.pojo.Friendships;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FriendshipsMapper extends BaseMapper<Friendships> {
    @Select(" (SELECT user2_id AS friend_id FROM friendships WHERE user1_id = #{userId}) " +
            "UNION " +
            "(SELECT user1_id AS friend_id FROM friendships WHERE user2_id = #{userId})")
    List<Integer> findAllFriendsByUserId(@Param("userId") Integer userId);
}
