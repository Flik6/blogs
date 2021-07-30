package com.coco52.entity;

import lombok.Data;

@Data
public class LoginMsgVO {
    private Integer code;
    private String msg;
    private MyUser myUser;
}
