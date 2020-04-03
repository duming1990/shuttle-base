package com.shuttle.base.config.security;

import com.shuttle.base.bean.UserInfo;
import com.shuttle.base.config.entity.JwtUser;
import com.shuttle.base.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailServiceImpl implements UserDetailsService {
  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired UserInfoService userInfoService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserInfo user = new UserInfo();
    user.setName(username);
    user = userInfoService.selectEntity(user);
    if (user == null) {
      throw new UsernameNotFoundException(
          String.format("No user found with username '%s'.", username));
    }
    user.setPassword(this.passwordEncoder.encode(user.getPassword()));
    // 权限
    List<GrantedAuthority> authorities = new ArrayList<>();
    if (username.equals("admin")) {
      authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("admin");
    } else {
      authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("test");
    }
    // 定义存放角色集合的对象
    List<String> roleList = new ArrayList<String>();
    for (GrantedAuthority grantedAuthority : authorities) {
      roleList.add(grantedAuthority.getAuthority());
    }
    user.setRoles("test");
    return new JwtUser(user);
  }
}
