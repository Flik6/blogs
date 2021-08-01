package com.coco52.config.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coco52.entity.Account;
import com.coco52.entity.Role;
import com.coco52.entity.RoleAccount;
import com.coco52.mapper.AccountMapper;
import com.coco52.mapper.RoleMapper;
import com.coco52.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        QueryWrapper<Account> accountWrapper = new QueryWrapper();
        accountWrapper.eq("username",name);
        Account account = accountMapper.selectOne(accountWrapper);
        if (account==null){
            throw new UsernameNotFoundException("用户名不存在!");
        }
        List<Role> roles = roleMapper.selectRolesByUserUuid(account.getUuid());
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (int i = 0; i < roles.size(); i++) {
            authorities.add(new SimpleGrantedAuthority(roles.get(i).getRoleName()));
        }
        User user = new User(account.getUsername(), account.getPassword(),authorities);
        return user;
    }
}
