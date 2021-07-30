package com.coco52.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@TableName("role_account")
public class RoleAccount {
    private String userUuid;
    private Integer roleId;
}
