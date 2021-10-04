package com.coco52.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignLog {
    @TableId(type = IdType.NONE)
    private Long id;
    private String browserName;
    private String browserVersion;
    private String browserEngine;
    private String getBrowserEngineVersion;
    private String osName;
    private String osPlatform;
    private LocalDateTime signTime;
}
