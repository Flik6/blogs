package com.coco52.controller;

import com.coco52.entity.RespMsg;
import com.coco52.entity.SchoolUser;
import com.coco52.service.CampusTodayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin
@Api(tags = "今日校园相关模块")
@RequestMapping("/school")

public class CampusTodayController {

    @Autowired
    private CampusTodayService campusTodayService;
    @ApiOperation("提交个人健康信息")
    @PostMapping("/submitHealth")
    public RespMsg submitHealth(@RequestBody SchoolUser user){
        System.out.println(user);
        RespMsg respMsg = campusTodayService.storageUser(user);
        return respMsg;
    }
}
