package com.coco52.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.coco52.entity.RespResult;
import com.coco52.entity.SignLog;
import com.coco52.mapper.blog.SignLogMapper;
import com.coco52.service.UtilService;
import com.coco52.util.MyUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.zip.ZipException;

@Service
@DS("master")
public class UtilServiceImpl implements UtilService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SignLogMapper signLogMapper;
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
        UserAgent ua = UserAgentUtil.parse(agent);

        SignLog signLog = new SignLog(
                null,
                ua.getBrowser().toString(),
                ua.getVersion(),
                ua.getEngine().toString(),
                ua.getEngineVersion(),
                ua.getOs().toString(),
                ua.getPlatform().toString(),
                LocalDateTime.now()
        );
        signLogMapper.insert(signLog);
        return b ? RespResult.success("打卡成功！", forEntity.getBody()) : RespResult.fail("Error，签到失败,参数可能填写错误！", forEntity.getBody());
    }

    @Override
    public RespResult parseJson(String jsons) {
        String unZlibHtml =null;
        byte[] decodeStr = Base64.decode(jsons);
        try {
            unZlibHtml = ZipUtil.unZlib(decodeStr, "utf-8");
        }catch (Exception e){
            return RespResult.fail(e.getMessage(),"解析时发生错误，请检查提交的content是否错误");
        }

        return RespResult.success("解析成功！", unZlibHtml);
    }

}