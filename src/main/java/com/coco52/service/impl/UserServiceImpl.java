package com.coco52.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coco52.entity.*;
import com.coco52.entity.RespMsg;
import com.coco52.mapper.AccountMapper;
import com.coco52.mapper.RoleAccountMapper;
import com.coco52.mapper.RoleMapper;
import com.coco52.mapper.UserMapper;
import com.coco52.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleAccountMapper roleAccountMapper;
    /**
     * 注册账号  操作两个数据库表  user表 account表
     * user表存储用户详细信息
     * account 表存储用户账号密码
     *
     * @param registerUser 用户上传进来的账号密码
     * @return 1注册成功  0注册失败  2账号已被注册
     */
    public RespMsg registerUser(Account registerUser) {
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.eq("username", registerUser.getUsername());
        Account account = accountMapper.selectOne(wrapper);
        if (account != null) {
            return RespMsg.fail("账号已被注册！");
        }
        registerUser.setUuid(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        registerUser.setPassword(new BCryptPasswordEncoder().encode(registerUser.getPassword()));
        int flag = accountMapper.insert(registerUser);
        MyUser myUser = new MyUser();
        myUser.setUuid(registerUser.getUuid());
        myUser.setState(0);
        myUser.setValidateCode(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        userMapper.insert(myUser);
        roleAccountMapper.insert(new RoleAccount(registerUser.getUuid(),5));
        if(flag==1){
            return RespMsg.success("注册成功！");
        }else {
            return RespMsg.success("未知错误，请联系管理员！");
        }
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
    public RespMsg login(Account loginAccount) {
        RespMsg respMsg = new RespMsg();
        QueryWrapper<Account> accountWrapper = new QueryWrapper<>();
        Account account = accountMapper.selectOne(accountWrapper.eq("username", loginAccount.getUsername()));
        if (account == null) {
            respMsg.setCode(0);
            respMsg.setMsg("用户尚未注册！请注册之后在尝试登录");
            respMsg.setData(null);
            return respMsg;
        }
        accountWrapper.eq("password", loginAccount.getPassword());
        Account account1 = accountMapper.selectOne(accountWrapper);
        if (account1 == null) {
            respMsg.setCode(2);
            respMsg.setMsg("用户密码错误,请重新登录！");
            respMsg.setData(null);
            return respMsg;
        }
        QueryWrapper<MyUser> userWrapper = new QueryWrapper<>();
        userWrapper.eq("uuid", account.getUuid());
        MyUser myUser = userMapper.selectOne(userWrapper);
        respMsg.setCode(1);
        respMsg.setMsg("登陆成功！");
        respMsg.setData(myUser);
        return respMsg;
    }




    @Override
    public MyUser selectByAccount(Account loginAccount) {
        QueryWrapper<Account> accountWrapper = new QueryWrapper();
        if (loginAccount.getUsername()!=null || loginAccount.getUsername()!=""){
            accountWrapper.eq("username", loginAccount.getUsername());
        }else if (loginAccount.getPassword()!=null || loginAccount.getPassword()!=""){
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
        accountWrapper.eq("username",username);
        Account account = accountMapper.selectOne(accountWrapper);


        return account;
    }

}
