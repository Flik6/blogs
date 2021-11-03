package com.coco52.entity.words;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Books {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String bookId;
    private String tag;
    private String origin;
    private String intro;
    private String imageUrl;
    private String wordNum;
    private Boolean status;
}
