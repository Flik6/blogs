package com.coco52.controller;

import com.coco52.entity.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ConfigController {
    @Autowired
    private Advice advice;

    @GetMapping("/getAdvice")
    @ResponseBody
    public String getAdvice(){
        return advice.getContent();
    }
    @GetMapping("/setAdvice")
    @ResponseBody
    public Advice setAdvice(String content){
        advice.setCode(200);
        advice.setContent(content);
        return advice;
    }

}
