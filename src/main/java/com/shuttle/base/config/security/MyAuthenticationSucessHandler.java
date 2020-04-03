package com.shuttle.base.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shuttle.base.config.entity.JwtUser;
import com.shuttle.base.constant.SecurityConstants;
import com.shuttle.base.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MyAuthenticationSucessHandler implements AuthenticationSuccessHandler {
  private ThreadLocal<Boolean> rememberMe = new ThreadLocal<>();

  private RequestCache requestCache = new HttpSessionRequestCache();
  private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

  @Autowired private ObjectMapper mapper;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    // 如果登录成功的相关逻辑
    JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
    List<String> roles =
        jwtUser.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
    System.out.println("#######################roles:" + roles);
    System.out.println("#######################rememberMe.get():" + rememberMe.get());
    // 创建 Token
    String token = JwtTokenUtils.createToken(jwtUser.getUsername(), roles, true);
    // Http Response Header 中返回 Token
    System.out.println("#######################token:" + token);
    response.setHeader(SecurityConstants.TOKEN_HEADER, token);
    response.setContentType("application/json;charset=utf-8");
    response.getWriter().write(mapper.writeValueAsString(authentication));
  }
}
