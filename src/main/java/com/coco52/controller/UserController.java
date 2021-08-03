package com.coco52.controller;

import com.coco52.entity.Account;
import com.coco52.entity.RespMsg;
import com.coco52.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 注册用户
     *
     * @param account
     * @return 注册用户  0 注册失败  1注册成功
     */
    @RequestMapping("/register")
    public RespMsg register(Account account) {
        System.out.println(account);
        RespMsg msg = userService.registerUser(account);
        return msg;
    }

    /**
     * 登录
     *
     * @param account
     * @return 1.null   2.user
     * null：
     */
    @RequestMapping("/login")
    public RespMsg login(Account account) {
        System.out.println(account);
        RespMsg msg = userService.login(account);
        if (msg == null) {
            return msg;
        }
        return msg;
    }

    @RequestMapping("/showUsers")
    public RespMsg showUser() {
        return RespMsg.success("showUsers");
    }

    @RequestMapping("/addUser")
    public RespMsg addUser() {
        return RespMsg.success("addUser");
    }

    @RequestMapping("/updateUser")
    public RespMsg updateUser() {
        return RespMsg.success("updateUser");
    }

    @RequestMapping("/delUser")
    public RespMsg delUser() {
        return RespMsg.success("delUser");
    }

    @RequestMapping("/banUser")
    public RespMsg banUser() {
        return RespMsg.success("banUser");
    }


}
