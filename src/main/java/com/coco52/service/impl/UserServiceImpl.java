package com.coco52.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coco52.entity.Account;
import com.coco52.entity.LoginMsgVO;
import com.coco52.entity.User;
import com.coco52.mapper.AccountMapper;
import com.coco52.mapper.UserMapper;
import com.coco52.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AccountMapper accountMapper;

    /**
     * 注册账号  操作两个数据库表  user表 account表
     * user表存储用户详细信息
     * account 表存储用户账号密码
     *
     * @param registerUser 用户上传进来的账号密码
     * @return 1注册成功  0注册失败  2账号已被注册
     */
    public int registerUser(Account registerUser) {
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.eq("username", registerUser.getUsername());
        Account account = accountMapper.selectOne(wrapper);
        if (account != null) {
            return 2;  // 此处是判断账号是否已注册 如果已注册就返回2
        }
        registerUser.setUuid(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        int flag = accountMapper.insert(registerUser);
        User user = new User();
        user.setUuid(registerUser.getUuid());
        user.setState(0);
        user.setValidateCode(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        userMapper.insert(user);
        return flag;
    }

    /**
     * 登录方法
     * @param loginAccount   用户登录的账号密码
     * @return   LoginMsgVO实体类   三个code值   0   1  2
     * 0  用户尚未注册
     * 1  用户登录成功
     * 2  用户密码错误
     */
    @Override
    public LoginMsgVO login(Account loginAccount) {
        LoginMsgVO loginMsgVO = new LoginMsgVO();
        QueryWrapper<Account> accountWrapper = new QueryWrapper<>();
        Account account = accountMapper.selectOne(accountWrapper.eq("username", loginAccount.getUsername()));
        if (account == null) {
            loginMsgVO.setCode(0);
            loginMsgVO.setMsg("用户尚未注册！请注册之后在尝试登录");
            loginMsgVO.setUser(null);
            return loginMsgVO;
        }
        accountWrapper.eq("password", loginAccount.getPassword());
        Account account1 = accountMapper.selectOne(accountWrapper);
        if (account1 == null) {
            loginMsgVO.setCode(2);
            loginMsgVO.setMsg("用户密码错误,请重新登录！");
            loginMsgVO.setUser(null);
            return loginMsgVO;
        }
        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.eq("uuid", account.getUuid());
        User user = userMapper.selectOne(userWrapper);
        loginMsgVO.setCode(1);
        loginMsgVO.setMsg("登陆成功！");
        loginMsgVO.setUser(user);
        return loginMsgVO;
    }


    @Override
    public User selectByAccount(Account loginAccount) {
        QueryWrapper<Account> accountWrapper = new QueryWrapper();
        accountWrapper.eq("username", loginAccount.getUsername());
        accountWrapper.eq("password", loginAccount.getPassword());
        Account account = accountMapper.selectOne(accountWrapper);
        if (account == null) {
            return null;
        }
        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.eq("uuid", account.getUuid());
        User user = userMapper.selectOne(userWrapper);
        return user;
    }
}
