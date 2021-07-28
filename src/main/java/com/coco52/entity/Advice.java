package com.coco52.entity;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Component
@Scope("singleton")
public class Advice {
    private Integer code;
    private String content;
}
