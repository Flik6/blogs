package com.coco52.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("articles")
public class Article {
    @TableId(value = "id",type = IdType.NONE)
    private Long id;
    private String uuid;
    private String title;
    private String content;

}
