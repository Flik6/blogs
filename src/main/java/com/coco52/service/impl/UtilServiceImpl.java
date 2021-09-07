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
import org.thymeleaf.util.DateUtils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UtilServiceImpl implements UtilService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MyUtils myUtils;

    @Override
    public RespMsg sign(String url, String userId) {
        Date parseDate = null;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isEmpty(url)) {
            return RespMsg.fail("url的参数不允许为null");
        }
        Map<String, String> urlParam = myUtils.getUrlParam(url);
        String e = urlParam.get("e");

        String text = "jparam={'empids': '" + userId + "', 'loctime': '" + sdf.format(date) + "', 'coordinate': '113.94837060116612,22.558772759333863', 'Location': '广东省深圳市南山区新西路9-南门', 'photoid': '', 'description': ''}";
        String s = null;
        try {
            s = new String(text.getBytes(), "utf-8");
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            return RespMsg.fail("错误，未知异常,请联系管理员处理", unsupportedEncodingException.getCause());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>(s, headers);

        ResponseEntity<String> forEntity = restTemplate.postForEntity("http://ehr.yadi-group.com:82/YDHR/m.aspx?e=" + e + "&s=AttLbsSign.SQL", entity, String.class);
        int statusCode = forEntity.getStatusCodeValue();
        if (statusCode != 200 || forEntity.getHeaders().containsKey("Location")) {
            return RespMsg.fail("Error，签到失败,参数URL可能填写错误！", forEntity.getBody());
        }
        JSONObject jsonObject = JSONObject.parseObject(forEntity.getBody());
        jsonObject.containsKey("message");
        String message = (String) jsonObject.get("message");
        System.out.println(message);
        try {
            parseDate = sdf.parse(message);
        } catch (ParseException parseException) {
            return RespMsg.fail("Error,网站返回解析异常", forEntity.getBody());
        }
        long l = date.getTime() - parseDate.getTime();
        boolean b = Math.abs(l) < 1000 * 60 * 10;
        System.out.println(l);
        return b ? RespMsg.success("打卡成功！", forEntity.getBody()) : RespMsg.fail("Error，签到失败,参数可能填写错误！", forEntity.getBody());
    }
}