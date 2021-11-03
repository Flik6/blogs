package com.coco52.entity.words;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Exams {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String question;
    private String answer;
    private String parse;
    private Integer questionType;
    private String choose;
    private Integer wordId;
}
