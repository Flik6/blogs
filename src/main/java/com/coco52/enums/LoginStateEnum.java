package com.coco52.enums;

import lombok.Data;


public enum LoginStateEnum {
    /**
     * 用户尚未注册
     */
    USER_NOT_REGISTER(0,"用户尚未注册"),
    /**
     * 用户登陆成功
     */
    USER_LOGIN_SUCCESS(1,"用户登陆成功"),
    /**
     * 用户密码错误
     */
    USER_PASSWORD_ERROR(2,"用户密码错误"),
    /**
     * 用户不允许登录
     */
    USER_NOT_ALLOW_LOGIN(3,"用户不允许登录"),
    /**
     * 用户已被封禁
     */
    USER_BANNED(4,"用户已被封禁");

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    LoginStateEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
