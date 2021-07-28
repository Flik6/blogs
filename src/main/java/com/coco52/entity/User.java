package com.coco52.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("users")
public class User {
    @TableId(type = IdType.AUTO)
    private Long Id;
    private String nickname;
    private String email;
    @TableField(value = "registerTime",fill = FieldFill.INSERT)
    private Date registerTime;
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    @TableField(value = "validateCode")
    private String validateCode;
    private Integer age;
    private Integer state;
    private String uuid;
}
