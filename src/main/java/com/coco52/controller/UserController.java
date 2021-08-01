package com.coco52.controller;

import com.coco52.entity.Account;
import com.coco52.entity.VO.RespMsg;
import com.coco52.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 注册用户
     * @param account
     * @return  注册用户  0 注册失败  1注册成功
     */
    @RequestMapping("/register")
    @ResponseBody
    public int register(Account account){
        System.out.println(account);
        int i = userService.registerUser(account);
        return i;
    }

    /**
     * 登录
     * @param account
     * @return   1.null   2.user
     * null：
     */
    @RequestMapping("/login")
    @ResponseBody
    public RespMsg login(Account account){
        RespMsg msg = userService.login(account);
        if (msg==null){
            return msg;
        }
        return msg;
    }


}
