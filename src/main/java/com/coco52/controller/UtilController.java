package com.coco52.controller;

import com.alibaba.fastjson.JSONObject;
import com.coco52.entity.RespMsg;
import com.coco52.service.UtilService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/util")
public class UtilController {
    @Autowired
    private UtilService utilService;

    @RequestMapping("/sign")
    public RespMsg sign(String url) {
        RespMsg sign = utilService.sign(url);
        return sign;
    }
}