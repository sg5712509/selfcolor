package com.example.demo.domain.mapper;

import com.example.demo.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User findBySocial(@Param("socialTypeCode") String socialTypeCode,
                      @Param("socialId") String socialId);

    int insertUser(User user);
    int updateUser(User user);
}

