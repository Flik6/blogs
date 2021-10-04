package com.coco52.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("articlesInfo")
@ApiModel("文章详细信息pojo")
public class ArticlesInfo {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("uuid")
    private String uuid;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("文章简介")
    private String intro;

    @ApiModelProperty("文章阅读数量")
    private Long readCounts;

    @ApiModelProperty(value = "文章所属类别",example = "1")
    private Integer category;

    @ApiModelProperty("文章图片url")
    private String articleImage;

    @ApiModelProperty("文章唯一id")
    private String articleId;

    @ApiModelProperty("文章发表时间")
    private LocalDateTime releaseTime;

    @ApiModelProperty("外链")
    private String externalLink;
}
