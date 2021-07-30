package com.coco52.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("account")
public class Account {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String uuid;
}
