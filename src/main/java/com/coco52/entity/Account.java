package com.coco52.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("account")
@ApiModel("用户登录实体类")
public class Account {
    @TableId(type = IdType.AUTO)
    private Long id;
    @ApiModelProperty(value = "用户名",required = true)
    private String username;
    @ApiModelProperty(value = "密码",required = true)
    private String password;
    private String email;
    private String uuid;
}
