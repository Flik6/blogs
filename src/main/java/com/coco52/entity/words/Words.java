package com.coco52.entity.words;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Words {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String word;
    private String pos;
    private String cnTran;
    private String enTran;
    private String rememberMethod;
    private String usPhone;
    private String ukPhone;
    private String bookId;

}
