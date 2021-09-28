package com.coco52.service;

import com.coco52.entity.*;
import com.coco52.entity.RespResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;

public interface UserService {
    RespResult registerUser(Account account);
    RespResult login(Account account);
    MyUser selectByAccount(Account account);
    Account selectByUsername(String username);
    RespResult banUser(MyUser myUser);
    RespResult delUser(MyUser myUser);
    RespResult updateUser(MyUser myUser,HttpServletRequest request);
    RespResult showUser(HttpServletRequest request);

    RespResult uploadFile(MultipartFile[] files);

    RespResult setAvatar(HttpServletRequest request,MultipartFile avatar);

    BufferedImage getAvatar(String uuid);
}
