package com.coco52.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Resource;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessLog {
    private Long id;
    private String browserName;
    private String browserType;
    private String browserGroup;
    private String browserManufacturer;
    private String browserRenderingEngine;
    private String browserVersion;
    private String osName;
    private String osGroup;
    private String osManufacturer;
    private String deviceType;
    private String ip;
    private Timestamp updateTime;
}
