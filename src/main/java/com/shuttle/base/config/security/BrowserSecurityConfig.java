package com.shuttle.base.config.security;

import com.shuttle.base.filter.JWTAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired private MyAuthenticationSucessHandler authenticationSucessHandler;

  @Autowired private MyAuthenticationFailureHandler authenticationFailureHandler;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // 添加JWT filter
    http.addFilterBefore(
            new JWTAuthorizationFilter(authenticationManager()),
            UsernamePasswordAuthenticationFilter.class) // 添加验证码校验过滤器
            .formLogin() // 表单登录
        .loginPage("/authentication/require")
        .loginProcessingUrl("/login")
        .successHandler(authenticationSucessHandler) // 处理登录成功
        .failureHandler(authenticationFailureHandler) // 处理登录失败
        .and()
        .authorizeRequests() // 授权配置
        .antMatchers("/authentication/require", "/login.html", "/css/**")
        .permitAll()
        .anyRequest() // 所有请求
        .authenticated() // 都需要认证
        .and()
        .csrf()
        .disable();

    // 禁用缓存
    http.headers().cacheControl();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
