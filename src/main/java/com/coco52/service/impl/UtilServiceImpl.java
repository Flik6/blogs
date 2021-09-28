package com.coco52.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.coco52.entity.AccessLog;
import com.coco52.entity.RespResult;
import com.coco52.mapper.AccessLogMapper;
import com.coco52.service.UtilService;
import com.coco52.util.MyUtils;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
public class UtilServiceImpl implements UtilService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AccessLogMapper accessLogMapper;
    @Autowired
    private MyUtils myUtils;

    @Override
    public RespResult sign(String url, String userId, HttpServletRequest request) {
        Date parseDate = null;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isEmpty(url)) {
            return RespResult.fail("url的参数不允许为null");
        }
        Map<String, String> urlParam = myUtils.getUrlParam(url);
        String e = urlParam.get("e");

        String text = "jparam={'empids': '" + userId + "', 'loctime': '" + sdf.format(date) + "', 'coordinate': '113.94837060116612,22.558772759333863', 'Location': '广东省深圳市南山区新西路9', 'photoid': '', 'description': ''}";
        String s = null;
        try {
            s = new String(text.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            return RespResult.fail("错误，未知异常,请联系管理员处理", unsupportedEncodingException.getCause());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<>(s, headers);
        ResponseEntity<String> forEntity = restTemplate.postForEntity("http://ehr.yadi-group.com:82/YDHR/m.aspx?e=" + e + "&s=AttLbsSign.SQL", entity, String.class);
        int statusCode = forEntity.getStatusCodeValue();
        if (statusCode != 200 || forEntity.getHeaders().containsKey("Location")) {
            return RespResult.fail("Error，签到失败,参数URL可能填写错误！", forEntity.getBody());
        }
        JSONObject jsonObject = JSONObject.parseObject(forEntity.getBody());
        jsonObject.containsKey("message");
        String message = (String) jsonObject.get("message");
        System.out.println(message);
        try {
            parseDate = sdf.parse(message);
        } catch (ParseException parseException) {
            return RespResult.fail("Error,网站返回解析异常", forEntity.getBody());
        }
        long l = date.getTime() - parseDate.getTime();
        boolean b = Math.abs(l) > 0;
        String agent = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(agent);
        Browser browser = userAgent.getBrowser();
        OperatingSystem os = userAgent.getOperatingSystem();

        AccessLog accessLog = new AccessLog(
                null,
                browser.getName(),
                browser.getBrowserType().getName(),
                browser.getGroup().toString(),
                browser.getManufacturer().toString(),
                browser.getRenderingEngine().toString(),
                userAgent.getBrowserVersion().toString(),
                os.getName(),
                os.getGroup().toString(),
                os.getManufacturer().toString(),
                os.getDeviceType().toString(),
                request.getRemoteAddr(),
                new Timestamp(new Date().getTime())
        );
        accessLogMapper.insert(accessLog);
        return b ? RespResult.success("打卡成功！", forEntity.getBody()) : RespResult.fail("Error，签到失败,参数可能填写错误！", forEntity.getBody());
    }
}