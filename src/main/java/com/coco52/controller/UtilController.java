package com.coco52.controller;

import com.coco52.entity.RespMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/util")
public class UtilController {
    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/sign")
    @Valid
    public RespMsg sign( String e) {
        if (StringUtils.isEmpty(e)){
            return RespMsg.fail("e的参数不允许为null");
        }
        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://ehr.yadi-group.com:82/YDHR/m.aspx?e=" + e + "&s=AttLbsSign.SQL", String.class);
        int statusCode = forEntity.getStatusCodeValue();
        HttpHeaders headers = forEntity.getHeaders();
        MediaType contentType = headers.getContentType();
        System.out.println(contentType);
        if ("text/html".contains(contentType.toString())){
            return RespMsg.fail("参数e可能已经过期！");
        }
        return RespMsg.success(forEntity.getBody());
    }
}