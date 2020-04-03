package com.shuttle.base.dao;

import com.shuttle.base.bean.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface UserInfoDao {

  UserInfo selectEntity(UserInfo user);
}
