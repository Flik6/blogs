package com.coco52.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.coco52.entity.RespMsg;
import com.coco52.service.UtilService;
import com.coco52.util.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@Service
public class UtilServiceImpl implements UtilService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MyUtils myUtils;

    @Override
    public RespMsg sign(String url,String userId) {
        if (StringUtils.isEmpty(url)) {
            return RespMsg.fail("url的参数不允许为null");
        }
        Map<String, String> urlParam = myUtils.getUrlParam(url);
        String e=urlParam.get("e");
        JSONObject json = new JSONObject();
        json.put("empids", userId);
        json.put("loctime", "2021-09-30 18:00:48");
        json.put("coordinate", "113.94837060116612,22.558772759333863");
        json.put("Location", "广东省深圳市南山区新西路");
        json.put("photoid", "");
        json.put("description", "");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("jparam="+json.toJSONString(), headers);
        URI uri = null;
        try {
            uri = new URI("http://ehr.yadi-group.com:82/YDHR/m.aspx?e=" + e + "&s=AttLbsSign.SQL");
        } catch (URISyntaxException uriSyntaxException) {
            uriSyntaxException.printStackTrace();
        }

        ResponseEntity<String> forEntity = restTemplate.postForEntity(uri, entity, String.class);
        int statusCode = forEntity.getStatusCodeValue();

        if (statusCode != 200 || forEntity.getHeaders().containsKey("Location") || forEntity.getBody().contains("errcode")) {
            return RespMsg.fail("Error，签到失败,参数可能填写错误！", forEntity.getBody());
        }
        return RespMsg.success("打卡成功！",forEntity.getBody());
    }
}
