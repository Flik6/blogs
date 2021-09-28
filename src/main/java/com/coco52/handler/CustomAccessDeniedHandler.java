package com.coco52.handler;

import com.alibaba.fastjson.JSON;
import com.coco52.entity.RespResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义的权限不足处理器
 *
 *
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setStatus(httpServletResponse.SC_FORBIDDEN);
        httpServletResponse.setContentType("application/json");
        PrintWriter writer = httpServletResponse.getWriter();
        RespResult fail = RespResult.fail("权限不足,请联系管理员!");
        fail.setCode(403);
        writer.write(JSON.toJSONString(fail));
        writer.flush();
        writer.close();
    }
}
