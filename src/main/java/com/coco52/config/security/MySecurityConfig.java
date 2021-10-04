package com.coco52.config.security;


import com.coco52.handler.CustomAccessDeniedHandler;
import com.coco52.handler.CustomAuthenticationEntryPoint;
import com.coco52.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
//开启spring security 权限控制注解
@EnableGlobalMethodSecurity(prePostEnabled = true)
/**
 * Spring security配置类
 * configure(HttpSecurity http)
 * 配置 需要放行的请求、以及设置login页等等
 *
 * configure(AuthenticationManagerBuilder auth)
 * 配置身份验证的一些东西 与用户有关
 * 例如  用户权限   用户密码校验等等、、
 *
 */
public class MySecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    private MyAuthenticationProvider myAuthenticationProvider;

    @Autowired
    private UserDetailsServiceImpl UserDetailsServiceImpl;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                //基于token 不需要session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .headers()
                .cacheControl();
        //      添加自定义未授权、未登录、登录结果返回
        http.exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(this.customAccessDeniedHandler);
//      添加jwt登录授权过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);



    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(
                        "/user/register",
                        "/user/login",
                        "/user/getAvatar/**",
                        "/article/**",
                        "/home/**",
                        "/sign",
                        "/util/**",
                        "/school/**",
                        "/schoolHelp",
                        "/health",
                        "/index",
                        "/",
                        "/css/**",
                        "/images/**",
                        "/favicon.ico",
                        "/doc.html",//   swagger2放行路径
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/v2/**",
                        "/swagger-ui.html/**"
                );
    }

    @Bean
    public Filter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }


    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        super.configure(auth);
//        auth.userDetailsService(UserDetailsServiceImpl);
//        auth.authenticationProvider(myAuthenticationProvider);
        auth.userDetailsService(UserDetailsServiceImpl);
    }

}
