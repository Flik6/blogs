package com.coco52.handler;

import com.alibaba.fastjson.JSON;
import com.coco52.entity.RespResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * 认证失败处理类 返回未授权
 * 用来解决匿名用户访问无权限资源时的异常
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException exception) throws IOException, ServletException {
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(httpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer = httpServletResponse.getWriter();
        RespResult fail = RespResult.fail("请求"+httpServletRequest.getRequestURI()+"-"+httpServletRequest.getMethod()+", 访问认证失败，没有访问权限!",exception.getMessage());
        fail.setCode(401);
        writer.write(JSON.toJSONString(fail));
        writer.flush();
        writer.close();
    }
}
