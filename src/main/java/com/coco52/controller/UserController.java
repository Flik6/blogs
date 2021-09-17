package com.coco52.controller;

import com.coco52.entity.Account;
import com.coco52.entity.MyUser;
import com.coco52.entity.RespMsg;
import com.coco52.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
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
    @RequestMapping("/login")
    public RespMsg login(@RequestBody Account account){
        return userService.login(account);
    }


    @RequestMapping("/showUsers")
    @PreAuthorize("hasAnyAuthority('admin','secretary')")
    public RespMsg showUser() {
        RespMsg respMsg = userService.showUser();
        return respMsg;
    }

    @RequestMapping("/addUser")
    @PreAuthorize("hasAnyAuthority('admin','secretary')")
    public RespMsg addUser(Account account) {
        RespMsg respMsg = userService.addUser(account);
        return respMsg;
    }

    @RequestMapping("/updateUser")
    @PreAuthorize("hasAnyAuthority('admin','secretary')")
    public RespMsg updateUser(MyUser myUser) {
        RespMsg respMsg = userService.updateUser(myUser);
        return respMsg;
    }

    @RequestMapping("/delUser")
    @PreAuthorize("hasAnyAuthority('admin','secretary')")
    public RespMsg delUser(MyUser myUser) {
        RespMsg respMsg = userService.delUser(myUser);
        return respMsg;
    }

    @RequestMapping("/banUser")
    @PreAuthorize("hasAnyAuthority('admin','secretary')")
    public RespMsg banUser(MyUser myUser) {
        RespMsg respMsg = userService.banUser(myUser);
        return respMsg;
    }


}
