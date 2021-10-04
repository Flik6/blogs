package com.coco52.filter;

import com.alibaba.fastjson.JSON;
import com.coco52.entity.RespResult;
import com.coco52.enums.ResultCode;
import com.coco52.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * jwt登录授权过滤器
 */

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(tokenHeader);
        if (authHeader!=null && authHeader.startsWith(tokenHead)){
            String authToken = authHeader.substring(tokenHead.length());
            String username = jwtTokenUtil.getUsernameFromToken(authToken);
            if (username==null){
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(RespResult.fail(ResultCode.TOKEN_HAS_EXPIRED,null)));
                writer.flush();
                writer.close();
                return;
            }
            // token 存在用户名  但用户未登录
            if (username!=null && null== SecurityContextHolder.getContext().getAuthentication()){
//                登录
                UserDetails userDetails = userDetailsService.   loadUserByUsername(username);
                if (jwtTokenUtil.validateToken(authToken,userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
