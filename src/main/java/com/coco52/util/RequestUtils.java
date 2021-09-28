package com.coco52.util;

import com.coco52.entity.RespResult;
import com.coco52.enums.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;

@Component
public class RequestUtils {

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public String getUUIDFromRequest(HttpServletRequest request){
        String allToken = request.getHeader(tokenHeader);
        String token = allToken.substring(tokenHead.length());
        String uuid = jwtTokenUtil.getUUIDFromToken(token);
        return uuid;
    }
}
