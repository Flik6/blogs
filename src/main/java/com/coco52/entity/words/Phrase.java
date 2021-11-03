package com.coco52.entity.words;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Phrase {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String sentence;
    private String translation;
    private String wordId;
}
