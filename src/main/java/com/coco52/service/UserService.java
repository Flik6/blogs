package com.coco52.service;

import com.coco52.entity.Account;
import com.coco52.entity.LoginMsgVO;
import com.coco52.entity.User;

public interface UserService {
    int registerUser(Account account);
    LoginMsgVO login(Account account);
    User selectByAccount(Account account);

}
