package com.coco52.service;

import com.coco52.entity.*;
import com.coco52.entity.RespMsg;

public interface UserService {
    RespMsg registerUser(Account account);
    RespMsg login(Account account);
    MyUser selectByAccount(Account account);
    Account selectByUsername(String username);
    RespMsg banUser(MyUser myUser);
    RespMsg delUser(MyUser myUser);
    RespMsg addUser(Account account);
    RespMsg updateUser(MyUser myUser);
    RespMsg showUser();
}
