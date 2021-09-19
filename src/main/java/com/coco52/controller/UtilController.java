package com.coco52.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.annotation.ResponseJSONP;
import com.coco52.entity.RespMsg;
import com.coco52.entity.SignIn;
import com.coco52.service.UtilService;
import io.swagger.annotations.Api;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/util")
@Api(tags = "打卡类模块")
public class UtilController {
    @Autowired
    private UtilService utilService;

    @PostMapping("/sign")
    @ResponseBody
    public RespMsg sign(@RequestBody SignIn signIn,HttpServletRequest request) {
        RespMsg sign = utilService.sign(signIn.getUrl(), signIn.getUserId(),request);
        return sign;
//        return RespMsg.success("111");
    }
}