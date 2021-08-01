package com.coco52.config.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coco52.entity.Account;
import com.coco52.entity.Permissions;
import com.coco52.entity.Role;
import com.coco52.entity.RoleAccount;
import com.coco52.mapper.AccountMapper;
import com.coco52.mapper.PermissionsMapper;
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
/**
 * 用户详情服务实现  通过loadUserByUsername
 * 通过username 来获取用户的详细信息（密码、权限、个人信息。。。。）
 */
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private PermissionsMapper permissionsMapper;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        QueryWrapper<Account> accountWrapper = new QueryWrapper();
        accountWrapper.eq("username",name);
        Account account = accountMapper.selectOne(accountWrapper);
        if (account==null){
            throw new UsernameNotFoundException("用户名不存在!");
        }
        List<Permissions> permissions = permissionsMapper.selectPermissionByAccount(account);
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (int i = 0; i < permissions.size(); i++) {
            authorities.add(new SimpleGrantedAuthority(permissions.get(i).getPermTag()));
        }
        System.out.println(authorities);
        User user = new User(account.getUsername(), account.getPassword(),authorities);
        return user;
    }
}
