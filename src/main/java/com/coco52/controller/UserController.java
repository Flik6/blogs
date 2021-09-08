package com.coco52.controller;

import com.coco52.entity.Account;
import com.coco52.entity.MyUser;
import com.coco52.entity.RespMsg;
import com.coco52.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
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
    public RespMsg register(@RequestBody Account account) {
        System.out.println(account);
        RespMsg msg = userService.registerUser(account);
        return msg;
    }


    @RequestMapping("/showUsers")
    @PreAuthorize("hasAnyRole('ROLE_admin','ROLE_secretary')")
    public RespMsg showUser() {
        return RespMsg.success("showUsers");
    }

    @RequestMapping("/addUser")
    @PreAuthorize("hasAnyRole('ROLE_admin','ROLE_secretary')")
    public RespMsg addUser() {
        return RespMsg.success("addUser");
    }

    @RequestMapping("/updateUser")
    @PreAuthorize("hasAnyRole('ROLE_admin','ROLE_secretary')")
    public RespMsg updateUser() {
        return RespMsg.success("updateUser");
    }

    @RequestMapping("/delUser")
    @PreAuthorize("hasAnyRole('ROLE_admin','ROLE_secretary')")
    public RespMsg delUser() {
        return RespMsg.success("delUser");
    }

    @RequestMapping("/banUser")
    @PreAuthorize("hasAnyRole('ROLE_admin','ROLE_secretary')")
    public RespMsg banUser(MyUser myUser) {
        return RespMsg.success("banUser");
    }


}
