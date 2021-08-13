package com.coco52.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("articlesInfo")
public class ArticlesInfo {
    private Long id;
    private String uuid;
    private Long readCounts;
    private String category;
    private String articleImage;
    private Long articleId;
}
