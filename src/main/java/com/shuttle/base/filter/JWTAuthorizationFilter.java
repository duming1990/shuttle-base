package com.shuttle.base.filter;

import com.shuttle.base.constant.SecurityConstants;
import com.shuttle.base.utils.JwtTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * 过滤器处理所有HTTP请求，并检查是否存在带有正确令牌的Authorization标头。例如，如果令牌未过期或签名密钥正确。
 *
 * @author shuang.kou
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

  private static final Logger logger = Logger.getLogger(JWTAuthorizationFilter.class.getName());

  public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
    super(authenticationManager);
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    String authorization = request.getHeader(SecurityConstants.TOKEN_HEADER);
    System.out.println("==========authorization=======" + authorization);
    // 如果请求头中没有token信息则直接放行了
    if (authorization == null || !authorization.startsWith(SecurityConstants.TOKEN_PREFIX)) {
      chain.doFilter(request, response);
      return;
    }
    // 如果请求头中有token，则进行解析，并且设置授权信息
    SecurityContextHolder.getContext().setAuthentication(getAuthentication(authorization));
    super.doFilterInternal(request, response, chain);
  }

  /** 获取用户认证信息 Authentication */
  private UsernamePasswordAuthenticationToken getAuthentication(String authorization) {
    String token = authorization.replace(SecurityConstants.TOKEN_PREFIX, "");
    // 验证token是否过期
    if (StringUtils.isNotEmpty(token) && new JwtTokenUtils().validateToken(token)) {
      try {
        String username = JwtTokenUtils.getUsernameByToken(token);
        logger.info("checking username:" + username);
        // 通过 token 获取用户具有的角色
        List<SimpleGrantedAuthority> userRolesByToken = JwtTokenUtils.getUserRolesByToken(token);
        for (int i = 0; i < userRolesByToken.size(); i++) {
          System.out.println("===token获取用户权限=====" + userRolesByToken.get(i).getAuthority());
        }

        if (!StringUtils.isEmpty(username)) {
          return new UsernamePasswordAuthenticationToken(username, null, userRolesByToken);
        }
      } catch (SignatureException
          | ExpiredJwtException
          | MalformedJwtException
          | IllegalArgumentException exception) {
        logger.warning(
            "Request to parse JWT with invalid signature . Detail : " + exception.getMessage());
      }
    }
    return null;
  }
}
