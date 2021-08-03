package com.coco52.config.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coco52.config.security.UserDetailsServiceImpl;
import com.coco52.entity.MyUser;
import com.coco52.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserDetailsServiceImpl userDetailService;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        UserDetails userDetails = userDetailService.loadUserByUsername(username);
        if (userDetails==null){
            throw new UsernameNotFoundException("用户名/密码无效");
        } else if (!userDetails.isEnabled()) {
            throw new DisabledException("用户已被禁用");
        } else if (!userDetails.isAccountNonExpired()) {
            throw new AccountExpiredException("账号已过期");
        } else if (!userDetails.isAccountNonLocked()) {
            throw new LockedException("账号已被锁定");
        } else if (!userDetails.isCredentialsNonExpired()) {
            throw new LockedException("凭证已过期");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean flag = encoder.matches(password, userDetails.getPassword());
        if (flag) {
            MyUser user = userMapper.selectUsersByUsername(username);
            user.setUpdateTime(new Date());
            user.setLastLoginTime(new Date());
            userMapper.update(user,new QueryWrapper<MyUser>().eq("uuid",user.getUuid()));
            return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }
}