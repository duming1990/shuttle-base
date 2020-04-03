package com.shuttle.base.config.entity;

import com.shuttle.base.bean.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/** @author D&M */
public class JwtUser implements UserDetails {

  private Integer id;
  private String username;
  private String password;
  private Collection<? extends GrantedAuthority> authorities;

  public JwtUser() {}

  /** 通过 user 对象创建jwtUser */
  public JwtUser(UserInfo user) {
    id = user.getId();
    username = user.getName();
    password = user.getPassword();
    authorities = user.getRoles();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public String toString() {
    return "JwtUser{"
        + "id="
        + id
        + ", username='"
        + username
        + '\''
        + ", password='"
        + password
        + '\''
        + ", authorities="
        + authorities
        + '}';
  }
}
