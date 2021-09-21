package com.coco52.controller;

import com.coco52.entity.Account;
import com.coco52.entity.MyUser;
import com.coco52.entity.RespMsg;
import com.coco52.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Api(tags = "用户相关模块")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "登录方法", notes = "只需要传入必要的参数")
    @PostMapping("/login")
    public RespMsg login(@RequestBody Account account) {
        return userService.login(account);
    }

    /**
     * 注册用户
     *
     * @param account
     * @return 注册用户  0 注册失败  1注册成功
     */
    @ApiOperation("注册用户")
    @PostMapping("/users")
    public RespMsg register(@RequestBody Account account) {
        System.out.println(account);
        RespMsg msg = userService.registerUser(account);
        return msg;
    }

    /**
     * 获取所有的users
     *
     * @return
     */
    @ApiOperation("查询所有用户")
    @GetMapping("/users")
    @PreAuthorize("hasAnyAuthority('admin','secretary')")
    public RespMsg showUser() {
        RespMsg respMsg = userService.showUser();
        return respMsg;
    }


    /**
     * 更新用户  信息
     *
     * @param myUser 只更新传入字段的的信息
     * @return
     */
    @ApiOperation(value = "更新用户信息", notes = "只更新传入字段的的信息")
    @PatchMapping("/users")
    @PreAuthorize("hasAnyAuthority('admin','secretary')")
    public RespMsg updateUser(@RequestBody MyUser myUser) {
        RespMsg respMsg = userService.updateUser(myUser);
        return respMsg;
    }

    /**
     * 更新用户息
     *
     * @param myUser 需要传入此用户的所有信息
     * @return
     */
    @ApiOperation(value = "更新用户", notes = "需要传入此用户的所有信息")
    @PutMapping("/users")
    @PreAuthorize("hasAnyAuthority('admin','secretary')")
    public RespMsg updateUserALL(@RequestBody MyUser myUser) {
        RespMsg respMsg = userService.updateUser(myUser);
        return respMsg;
    }

    /**
     * 删除用户~
     *
     * @param myUser 实际只需传入uuid
     * @return
     */
    @ApiOperation("删除用户")
    @DeleteMapping("/users")
    @PreAuthorize("hasAnyAuthority('admin','secretary')")
    public RespMsg delUser(@RequestBody MyUser myUser) {
        RespMsg respMsg = userService.delUser(myUser);
        return respMsg;
    }

    /**
     * 封禁用户
     *
     * @param myUser 实际只需传 uuid
     * @return
     */
    @ApiOperation("封禁用户")
    @PostMapping("/banUser")
    @PreAuthorize("hasAnyAuthority('admin','secretary')")
    public RespMsg banUser(@RequestBody MyUser myUser) {
        RespMsg respMsg = userService.banUser(myUser);
        return respMsg;
    }


}
