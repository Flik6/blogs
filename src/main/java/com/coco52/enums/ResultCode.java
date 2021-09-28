package com.coco52.enums;

import io.swagger.models.auth.In;
import lombok.Data;


public enum ResultCode {
    /**
         下面是常见的HTTP状态码：
         200 - 请求成功
         301 - 资源（网页等）被永久转移到其它URL
         404 - 请求的资源（网页等）不存在
         500 - 内部服务器错误

         #1000～1999 区间表示参数错误
         #2000～2999 区间表示用户错误
         #3000～3999 区间表示接口异常
     */
    //请求成功200-299
    SUCCESS(200,"请求成功"),
    FAIL(499,"请求失败"),
    SUCCESS_SUBMIT(201,"提交成功"),
    SUCCESS_SIGN(201,"签到成功"),
    //用户相关错误 2000-2999
    USER_UNKNOWN_ERROR(2999,"未知错误"),
    USERNAME_PASSWORD_ERROR(2001,"用户名或密码错误"),
    USERNAME_NOT_EXISTS(2002,"用户名不存在"),
    USER_HAS_EXISTED(2003,"用户已存在"),
    USER_PERMISSION_NOT_ENOUGH(2004,"权限不足"),
    USER_AVATAR_UPLOAD_ERROR(2005,"头像上传失败，请重试！"),
    USER_AVATAR_UPLOAD_SUCCESS(2006,"头像上传成功！"),

    //token相关结果
    TOKEN_NOT_EXIST(3000,"请求移除，未获取到Token值！"),

    //文件类相关操作 (1000-1100)
    File_UPLOAD_SUCCESS(1000,"文件上传成功~"),
    FILE_PART_UPLOAD_SUCCESS(1001,"部分文件上传成功"),
    FILE_WRITE_ERROR(1002,"服务器文件写入异常,我也不知道怎么回事 u(ToT)/~~~"),
    FILE_IS_NULL(1003,"啊,怎么什么文件也没有啊,u{{{(>_<)}}}"),
    FILE_UPLOAD_ERROR(1004,"文件上传失败，请重新上传!"),
    FILE_TOO_BIG(1005,"文件太大了，重新选个小一点的吧~ u`(*>﹏<*)′"),
    FILE_NOT_FOUND(1005,"文件没找到...u`(*>﹏<*)′")
    ;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private Integer code;
    private String message;
}
