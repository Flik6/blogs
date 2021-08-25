package com.coco52.config.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coco52.entity.*;
import com.coco52.mapper.*;
import com.coco52.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
/**
 * 用户详情服务实现  通过loadUserByUsername
 * 通过username 来获取用户的详细信息（密码、权限、个人信息。。。。）
 */
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleAccountMapper roleAccountMapper;
    @Override
    public UserDetails loadUserByUsername(String name) throws AuthenticationException {
        QueryWrapper<Account> accountWrapper = new QueryWrapper();
        accountWrapper.eq("username", name);
        Account account = accountMapper.selectOne(accountWrapper);
        if (StringUtils.isEmpty(account)) {
            throw new UsernameNotFoundException("用户名不存在!");
        }
//        List<Permissions> permissions = permissionsMapper.selectPermissionByAccount(account);
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<RoleAccount> roleList = roleAccountMapper.selectList(new QueryWrapper<RoleAccount>().eq("userUuid", account.getUuid()));

        for (int i = 0; i < roleList.size(); i++) {
            Role role = roleMapper.selectOne(new QueryWrapper<Role>().eq("id", roleList.get(i).getRoleId().toString()));
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        MyUser myUser = userMapper.selectUsersByUsername(name);
        User user = new User(account.getUsername(),
                account.getPassword(),
                myUser.getIsAvailable(),// 账户是否可用
                !myUser.getIsExpires(),//账户没有过期
                !myUser.getIsExpires(),//密码没有过期
                !myUser.getIsLock(),//账户没有锁定
                authorities);//权限列表
        return user;
    }
}
