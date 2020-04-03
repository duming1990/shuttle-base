package com.shuttle.base.service.impl;

import com.shuttle.base.bean.UserInfo;
import com.shuttle.base.dao.UserInfoDao;
import com.shuttle.base.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userInfoService")
public class UserInfoserviceImpl implements UserInfoService {
  @Autowired UserInfoDao userInfoDao;

  @Override
  public UserInfo selectEntity(UserInfo user) {
    return userInfoDao.selectEntity(user);
  }
}
