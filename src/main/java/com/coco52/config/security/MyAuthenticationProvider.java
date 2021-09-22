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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

//@Component
public class MyAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsServiceImpl userDetailService;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        UserDetails userDetails = userDetailService.loadUserByUsername(username);
        if (StringUtils.isEmpty(userDetails)) {
            throw new UsernameNotFoundException("请检查用户名是否输入错误！");
        } else if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("密码输入错误");
        } else if (!userDetails.isEnabled()) {
            throw new DisabledException("用户已被禁用");
        } else if (!userDetails.isAccountNonExpired()) {
            throw new AccountExpiredException("账号已过期");
        } else if (!userDetails.isAccountNonLocked()) {
            throw new LockedException("账号已被锁定");
        } else if (!userDetails.isCredentialsNonExpired()) {
            throw new LockedException("凭证已过期");
        }
        MyUser user = userMapper.selectUsersByUsername(username);
        LocalDateTime localDateTime = LocalDateTime.now();
        user.setUpdateTime(localDateTime);
        user.setLastLoginTime(localDateTime);
        userMapper.update(user, new QueryWrapper<MyUser>().eq("uuid", user.getUuid()));
        return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());

    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }
}