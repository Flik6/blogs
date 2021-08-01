package com.coco52.service;

import com.coco52.entity.*;
import com.coco52.entity.VO.RespMsg;

public interface UserService {
    int registerUser(Account account);
    RespMsg login(Account account);
    MyUser selectByAccount(Account account);
    Account selectByUsername(String username);
}
