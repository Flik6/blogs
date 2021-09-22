package com.coco52.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@TableName("users")
@ApiModel(value = "用户详细信息实体类", description = "用户详细信息实体类")
public class MyUser {
    @ApiModelProperty(value = "用户id索引", required = false)
    @TableId(type = IdType.AUTO)
    private Long Id;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty(value = "注册时间", required = false)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime registerTime;

    @ApiModelProperty(value = "更新时间", required = false)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime  updateTime;

    @ApiModelProperty("验证码")
    private String validateCode;

    @ApiModelProperty(value = "年龄", example = "18")
    private Integer age;

    @ApiModelProperty(value = "状态", example = "0")
    private Integer state;

    @ApiModelProperty("用户唯一标识")
    private String uuid;

    @ApiModelProperty("是否可用")
    @TableField(fill = FieldFill.INSERT)
    private Boolean isAvailable;

    @ApiModelProperty("是否过期")
    @TableField(fill = FieldFill.INSERT)
    private Boolean isExpires;

    @ApiModelProperty("是否锁定")
    @TableField(fill = FieldFill.INSERT)
    private Boolean isLock;

    @ApiModelProperty("上次登录时间")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty("头像")
    private String avatar;

}
