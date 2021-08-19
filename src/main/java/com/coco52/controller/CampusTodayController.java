package com.coco52.controller;

import com.coco52.entity.RespMsg;
import com.coco52.entity.SchoolUser;
import com.coco52.service.CampusTodayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/school")
public class CampusTodayController {

    @Autowired
    private CampusTodayService campusTodayService;

    @PostMapping("/submitHealth")
    public RespMsg submitHealth(@RequestBody SchoolUser user){
        System.out.println(user);
        RespMsg respMsg = campusTodayService.storageUser(user);
        return respMsg;
    }
}
