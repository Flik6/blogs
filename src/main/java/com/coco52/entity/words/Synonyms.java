package com.coco52.entity.words;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Synonyms {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String word;
    private String translation;
    private String pos;
    private String wordId;
}
