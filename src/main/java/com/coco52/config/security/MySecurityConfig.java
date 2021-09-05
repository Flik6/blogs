package com.coco52.config.security;


import com.coco52.entity.Permissions;
import com.coco52.mapper.PermissionsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

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

    @Autowired
    private MyAuthenticationProvider myAuthenticationProvider;

    @Autowired
    private UserDetailsServiceImpl UserDetailsServiceImpl;

    @Autowired
    private PermissionsMapper permissionsMapper;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.rememberMe();

        http.formLogin().loginPage("/login").failureUrl("/login?error=true").defaultSuccessUrl("/main?success")
                .permitAll().and().logout().logoutSuccessUrl("/login").and()
                .authorizeRequests().antMatchers("/register","/sign","/util/**","/school/**","/schoolHelp","/health", "/index","/").permitAll()
                .antMatchers("/getCarousel").permitAll()
                .antMatchers("/**").authenticated()
                .and().csrf().disable()
                //开启跨域
                .cors();

    }
    @Bean
    protected PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**","/images/**","/favicon.ico","/**/*.ico");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        super.configure(auth);
//        auth.userDetailsService(UserDetailsServiceImpl);
//        auth.authenticationProvider(myAuthenticationProvider);
        auth.userDetailsService(UserDetailsServiceImpl);
    }
}
