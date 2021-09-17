package com.coco52.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.coco52.entity.*;
import com.coco52.entity.RespMsg;
import com.coco52.mapper.AccountMapper;
import com.coco52.mapper.RoleAccountMapper;
import com.coco52.mapper.RoleMapper;
import com.coco52.mapper.UserMapper;
import com.coco52.service.UserService;
import com.coco52.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleAccountMapper roleAccountMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    /**
     * 注册账号  操作两个数据库表  user表 account表
     * user表存储用户详细信息
     * account 表存储用户账号密码
     *
     * @param registerUser 用户上传进来的账号密码
     * @return 1注册成功  0注册失败  2账号已被注册
     */
    @Transactional(rollbackFor = Exception.class)
    public RespMsg registerUser(Account registerUser) {
        System.out.println(registerUser.getPassword());
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.eq("username", registerUser.getUsername());
        Account account = accountMapper.selectOne(wrapper);
        if (account != null) {
            return RespMsg.fail("账号已被注册,请更改您的用户名！");
        }
        registerUser.setUuid(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        registerUser.setPassword(passwordEncoder.encode(registerUser.getPassword()));
        int flag = accountMapper.insert(registerUser);
        MyUser myUser = new MyUser();
        myUser.setUuid(registerUser.getUuid());
        myUser.setState(0);
        myUser.setValidateCode(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        userMapper.insert(myUser);
        roleAccountMapper.insert(new RoleAccount(null, registerUser.getUuid(), 3));
        if (flag == 1) {
            return RespMsg.success("注册成功！");
        } else {
            return RespMsg.success("未知错误，请联系管理员！");
        }
    }

    /**
     * 登录方法
     *
     * @param loginAccount 用户登录的账号密码
     * @return LoginMsgVO实体类   三个code值   0   1  2
     * 0  用户尚未注册
     * 1  用户登录成功
     * 2  用户密码错误
     */
    @Override
    public RespMsg login(Account loginAccount) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginAccount.getUsername());
        if (userDetails == null || !passwordEncoder.matches(loginAccount.getPassword(), userDetails.getPassword())) {
            return RespMsg.fail("用户名或密码错误！");
        } else if (!userDetails.isEnabled()) {
            return RespMsg.fail("账户被禁用,请联系管理员！");
        } else if (!userDetails.isAccountNonLocked()) {
            return RespMsg.fail("账号已被锁定,请联系管理员！");
        } else if (!userDetails.isCredentialsNonExpired()) {
            return RespMsg.fail("密码凭证已失效,请联系管理员！");
        } else if (!userDetails.isAccountNonExpired()) {
            return RespMsg.fail("账号已过期,请联系管理员！");
        }
        //更新security登录用户对象
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return RespMsg.success("登陆成功", tokenMap);
    }


    @Override
    public MyUser selectByAccount(Account loginAccount) {
        QueryWrapper<Account> accountWrapper = new QueryWrapper();
        if (loginAccount.getUsername() != null || loginAccount.getUsername() != "") {
            accountWrapper.eq("username", loginAccount.getUsername());
        } else if (loginAccount.getPassword() != null || loginAccount.getPassword() != "") {
            accountWrapper.eq("password", loginAccount.getPassword());
        }
        Account account = accountMapper.selectOne(accountWrapper);
        if (account == null) {
            return null;
        }
        QueryWrapper<MyUser> userWrapper = new QueryWrapper<>();
        userWrapper.eq("uuid", account.getUuid());
        MyUser myUser = userMapper.selectOne(userWrapper);
        return myUser;
    }

    @Override
    public Account selectByUsername(String username) {
        QueryWrapper<Account> accountWrapper = new QueryWrapper();
        accountWrapper.eq("username", username);
        Account account = accountMapper.selectOne(accountWrapper);
        return account;
    }

    @Override
    public RespMsg banUser(MyUser myUser) {
        if (ObjectUtils.isEmpty(myUser.getUuid())) {
            return RespMsg.fail("UUID为空,请检查之后重试！");
        }
        UpdateWrapper<MyUser> myUserUpdateWrapper = new UpdateWrapper<>();
        myUserUpdateWrapper.eq("uuid", myUser.getUuid());
        myUserUpdateWrapper.set("isLock", true);
        int update = userMapper.update(null, myUserUpdateWrapper);
        return update == 1 ? RespMsg.success("成功封禁用户！", myUser.getUuid()) : RespMsg.fail("封禁失败,用户可能！", myUser.getUuid());
    }

    /**
     * 删除用户
     *
     * @param myUser 用户实体  此参数内uuid不能为空
     * @return
     */
    @Override
    public RespMsg delUser(MyUser myUser) {
        if (ObjectUtils.isEmpty(myUser.getUuid())) {
            return RespMsg.fail("UUID为空,请检查之后重试！");
        }
        UpdateWrapper<MyUser> myUserUpdateWrapper = new UpdateWrapper<>();
        myUserUpdateWrapper.eq("uuid", myUser.getUuid());
        myUserUpdateWrapper.set("isAvailable", true);
        int update = userMapper.update(null, myUserUpdateWrapper);
        return update == 1 ? RespMsg.success("成功删除用户！", myUser.getUuid()) : RespMsg.fail("删除用户失败,用户可能不存在！", myUser.getUuid());
    }

    /**
     * 调用注册方法，直接注册用户
     *
     * @param account
     * @return
     */
    @Override
    public RespMsg addUser(Account account) {
        RespMsg respMsg = this.registerUser(account);
        return respMsg;
    }

    @Override
    public RespMsg updateUser(MyUser myUser) {
        UpdateWrapper<MyUser> myUserUpdateWrapper = new UpdateWrapper<>();
        myUserUpdateWrapper.eq("uuid", myUser.getUuid());
        int update = userMapper.update(myUser, myUserUpdateWrapper);
        return update == 1 ? RespMsg.success("成功更新用户！", myUser.getUuid()) : RespMsg.fail("更新用户失败,用户可能不存在！", myUser.getUuid());
    }

    @Override
    public RespMsg showUser() {
        List<MyUser> myUsers = userMapper.selectList(null);
        return RespMsg.success("查询成功！", myUsers);
    }
}
