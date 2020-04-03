package com.shuttle.base.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@ApiModel(value = "Student")
@Data
public class UserInfo implements Serializable {
  private static final long serialVersionUID = -1L;

  @ApiModelProperty(value = "主id")
  private int id;

  @ApiModelProperty(value = "姓名")
  private String name;

  @ApiModelProperty(value = "性别")
  private int sex;

  @ApiModelProperty(value = "年龄")
  private int age;

  @ApiModelProperty(value = "密码")
  private String password;

  @ApiModelProperty(value = "编号")
  private String no;

  @ApiModelProperty(value = "添加日期")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date add_date;

  @ApiModelProperty(value = "角色")
  private String roles;

  public List<SimpleGrantedAuthority> getRoles() {
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    Arrays.stream(roles.split(","))
        .forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
    return authorities;
  }
}
