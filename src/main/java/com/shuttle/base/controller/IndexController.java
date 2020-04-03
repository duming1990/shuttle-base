package com.shuttle.base.controller;

import ch.qos.logback.classic.Logger;
import com.shuttle.base.bean.UserInfo;
import com.shuttle.base.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(value = "用户Controller")
@RestController
public class IndexController {
  private static final Logger logger = (Logger) LoggerFactory.getLogger(IndexController.class);

  @Autowired private UserInfoService userInfoService;

  @ApiOperation(value = "获取用户信息", notes = "根据用户id获取用户信息")
  @ApiImplicitParam(
      name = "id",
      value = "用户id",
      required = true,
      dataType = "Integer",
      paramType = "path")
  @RequestMapping(value = "/getUserById/{id}", method = RequestMethod.GET)
  public UserInfo getUserById(@PathVariable(name = "id") Integer id) {
    UserInfo user = new UserInfo();
    user.setId(id);
    user = this.userInfoService.selectEntity(user);
    return user;
  }

  @GetMapping("/auth/admin")
  @PreAuthorize("hasAuthority('admin')")
  public String authenticationTest() {
    return "您拥有admin权限，可以查看";
  }
}
