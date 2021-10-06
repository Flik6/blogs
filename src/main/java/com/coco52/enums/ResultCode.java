package com.coco52.enums;


public enum ResultCode {
    /**
         下面是常见的HTTP状态码：
         200 - 请求成功
         301 - 资源（网页等）被永久转移到其它URL
         404 - 请求的资源（网页等）不存在
         500 - 内部服务器错误

         #1000～1100 区间表示文件类相关操作
         #2000～2999 区间表示用户类相关操作
         #3000～3999 区间表示接口异常
         #4000～4199 区间表示接口异常
     */
    //请求成功200-299
    SUCCESS(200,"请求成功"),
    FAIL(402,"请求失败"),
    SUCCESS_SUBMIT(201,"提交成功"),
    SUCCESS_SIGN(201,"签到成功"),
    //token相关结果
    TOKEN_NOT_EXIST(401,"请求异常，未获取到Token值！"),
    TOKEN_HAS_EXPIRED(499,"token已过期,请重新登陆!"),

    //文件类相关操作 (1000-1199)  1000-1099 成功   1100-1199 错误
    File_UPLOAD_SUCCESS(1000,"文件上传成功~"),
    FILE_PART_UPLOAD_SUCCESS(1001,"部分文件上传成功"),

    FILE_WRITE_ERROR(1101,"服务器文件写入异常,我也不知道怎么回事 u(ToT)/~~~"),
    FILE_IS_NULL(1102,"啊,怎么什么文件也没有啊,u{{{(>_<)}}}"),
    FILE_UPLOAD_ERROR(1103,"oh,shit,文件没上传成功"),
    FILE_TOO_BIG(1104,"文件太大了，重新选个小一点的吧~ u`(*>﹏<*)′"),
    FILE_NOT_FOUND(1105,"文件没找到...u`(*>﹏<*)′"),


    //用户类相关操作 2000-2099 成功   2100-2199 错误
    USER_LOGIN_SUCCESS(2000,"登陆成功！"),
    USER_REGISTER_SUCCESS(2001,"注册成功！"),
    USER_BAN_SUCCESS(2002,"成功封禁用户！"),
    USER_PROFILE_UPDATED_SUCCESSFULLY(2003,"个人资料更新成功！"),

    USERNAME_PASSWORD_ERROR(2100,"用户名或密码错误"),
    USERNAME_NOT_EXISTS(2101,"用户名不存在"),
    USER_HAS_EXISTED(2102,"用户已存在"),
    USER_PERMISSION_NOT_ENOUGH(2103,"权限不足"),
    USER_AVATAR_UPLOAD_ERROR(2104,"头像上传失败，请重试！"),
    USER_AVATAR_UPLOAD_SUCCESS(2105,"头像上传成功！"),
    USER_HAS_BEEN_REGISTERED(2106,"账号已被注册,请更改您的用户名！"),
    USER_ACCOUNT_NOT_EXIST(2107,"账户不存在,请联系管理员！"),
    USER_ACCOUNT_HAS_BEEN_BAN(2108,"账号已被封禁,请联系管理员！"),
    USER_PASSWORD_HAS_EXPIRED(2109,"密码凭证已过期,请联系管理员！"),
    USER_ACCOUNT_HAS_EXPIRED(2110,"账号已过期,请联系管理员！"),
    USER_BAN_FAIL(2111,"封禁失败,用户可能已被封禁！"),
    USER_UUID_iS_EMPTY(2112,"UUID为空,请检查之后重试！"),
    USER_PROFILE_UPDATED_FAIL(2113,"更新个人资料失败,用户可能不存在！"),
    USER_UNKNOWN_ERROR(2199,"未知错误，请联系管理员！"),



    //文章类相关错误
    ARTICLE_SUCCESSFULLY_PUBLISHED(4000,"文章发表成功!"),
    ARTICLE_GET_SUCCESS(40011,"文章获取成功!"),
    ARTICLE_INFO_GET_SUCCESS(40021,"文章详细信息获取成功!"),

    ARTICLE_NOT_ALLOW_NULL(4101,"文章标题或内容为空,请检查"),
    ARTICLE_NOT_GET_CONTENT(4102,"oh,no,文章找不到了 o((>ω< ))o"),
    ARTICLE_INFO_GET_ERROR(4103,"怎么找不到这篇文章的详细信息呢 u＞︿＜"),
    ARTICLE_TOO_SHORT(4104,"啊啊啊，不可以这么短啊 o((>ω< ))o")

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
