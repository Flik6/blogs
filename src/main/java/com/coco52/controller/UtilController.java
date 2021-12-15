package com.coco52.controller;

import com.coco52.entity.RespResult;
import com.coco52.entity.SignIn;
import com.coco52.service.UtilService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/util")
@Api(tags = "打卡类模块")
public class UtilController {
    @Autowired
    private UtilService utilService;

    @PostMapping("/sign")
    @ResponseBody
    public RespResult sign(@RequestBody SignIn signIn, HttpServletRequest request) {
        RespResult sign = utilService.sign(signIn.getUrl(), signIn.getUserId(),request);
        return sign;
//        return RespMsg.success("111");
    }

    @PostMapping("/parse")
    @ResponseBody
    public RespResult parse(@RequestBody String jsons) {
        RespResult sign = utilService.parseJson(jsons);
        return sign;
//        return RespMsg.success("111");
    }


}