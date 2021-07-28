package com.coco52.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Account {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String uuid;
}
