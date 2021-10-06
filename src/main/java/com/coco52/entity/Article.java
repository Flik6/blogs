package com.coco52.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("文章pojo")
public class Article {
    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("uuid")
    private String uuid;
    @ApiModelProperty("文章唯一id")
    private String articleId;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("文章内容")
    private String content;

    @ApiModelProperty("文章简介")
    @TableField(exist = false)
    private String intro;

    @ApiModelProperty("文章分类")
    @TableField(exist = false)
    private String category;

    @ApiModelProperty(value = "外部链接")
    @TableField(exist = false)
    private String externalLink;

    @ApiModelProperty(value = "文章图片")
    @TableField(exist = false)
    private String articleImage;
}
