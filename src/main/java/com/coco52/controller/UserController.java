package com.coco52.controller;

import com.coco52.entity.Account;
import com.coco52.entity.MyUser;
import com.coco52.entity.RespResult;
import com.coco52.enums.ResultCode;
import com.coco52.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;

@RestController
@RequestMapping("/user")
@Api(tags = "用户相关模块")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 登录方法
     * @param account
     * @return
     */
    @ApiOperation(value = "登录方法", notes = "只需要传入用户名 密码")
    @PostMapping("/login")
    public RespResult login(@RequestBody Account account) {
        return userService.login(account);
    }

    /**
     * 注册用户
     *
     * @param account
     * @return 注册用户  0 注册失败  1注册成功
     */
    @ApiOperation("注册用户")
    @PostMapping("/register")
    public RespResult register(@RequestBody Account account) {
        System.out.println(account);
        RespResult msg = userService.registerUser(account);
        return msg;
    }

    /**
     * 获取所有的users
     *
     * @return
     */
    @ApiOperation("根据Headers 内的jwt 查询用户信息")
    @GetMapping("/users")
    @PreAuthorize("hasAnyAuthority('admin','secretary','user')")
    public RespResult getUserInfo(HttpServletRequest request) {
        RespResult respResult = userService.showUser(request);
        return respResult;
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
    public RespResult updateUser(@RequestBody MyUser myUser,HttpServletRequest request) {
        RespResult respResult = userService.updateUser(myUser,request);
        return respResult;
    }

    /**
     * 更新用户息
     *
     * @param myUser 需要传入此用户的所有信息
     * @return
     */
    @ApiOperation(value = "更新用户", notes = "需要传入此用户的所有信息")
    @PutMapping("/users")
    @PreAuthorize("hasAnyAuthority('admin','secretary','user')")
    public RespResult updateUserALL(@RequestBody MyUser myUser,HttpServletRequest request) {
        RespResult respResult = userService.updateUser(myUser,request);
        return respResult;
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
    public RespResult delUser(@RequestBody MyUser myUser) {
        RespResult respResult = userService.delUser(myUser);
        return respResult;
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
    public RespResult banUser(@RequestBody MyUser myUser) {
        RespResult respResult = userService.banUser(myUser);
        return respResult;
    }

    @ApiOperation("上传文件")
    @PostMapping("/upload")
    @PreAuthorize("hasAnyAuthority('admin','secretary','user')")
    public RespResult uploadFiles(@RequestParam("files") MultipartFile[] files){
        RespResult respResult=userService.uploadFile(files);
        return respResult;
    }
    @ApiOperation("设置用户头像")
    @PostMapping("/setAvatar")
    @PreAuthorize("hasAnyAuthority('admin','secretary','user')")
    public RespResult setAvatar(HttpServletRequest request,@RequestParam("avatar") MultipartFile avatar){

        RespResult respResult=userService.setAvatar(request,avatar);
        return respResult;
    }

    @ApiOperation("获取头像")
    @GetMapping("/getAvatar/{uuid}")
    public RespResult getAvatar(@PathVariable("uuid") String uuid, HttpServletResponse response){
        // img为图片的二进制流
        BufferedImage image=userService.getAvatar(uuid);
        if (ObjectUtils.isEmpty(image)){
            return RespResult.fail(ResultCode.FILE_NOT_FOUND.getCode(),ResultCode.FILE_NOT_FOUND.getMessage(),null);
        }

        try {
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(image,"png",outputStream);
        } catch (IOException e) {
            return RespResult.fail(ResultCode.FILE_NOT_FOUND.getCode(),ResultCode.FILE_NOT_FOUND.getMessage(),null);
        }
        return RespResult.fail(ResultCode.SUCCESS.getCode(),ResultCode.SUCCESS.getMessage(),null);
    }

}
