package com.coco52.service;

import com.coco52.entity.*;

public interface UserService {
    int registerUser(Account account);
    LoginMsgVO login(Account account);
    MyUser selectByAccount(Account account);
    Account selectByUsername(String username);
}
