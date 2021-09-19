package com.coco52.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("schoolUser")
@ApiModel("今日校园模块类实体")
public class SchoolUser {
    @ApiModelProperty(value = "用户id",required = false,hidden = true)
    @TableId(type = IdType.AUTO)
    private Long id;
    @ApiModelProperty(value = "学号 例如:190120101",required = true)
    private String username;
    @ApiModelProperty(value = "今日校园密码",required = true)
    private String password;
    @ApiModelProperty(value = "邮箱",required = true)
    private String email;
    @ApiModelProperty(value = "您当前所处位置",required = true)
    private String address;
    @ApiModelProperty(value = "用户信息创建时间",required = false,hidden = true)
    private Timestamp createTime;
}
