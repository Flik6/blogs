package com.coco52.controller;

import com.coco52.entity.RespResult;
import com.coco52.service.ArticleService;
import com.coco52.service.HomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@RestController
//@CrossOrigin
@RequestMapping("/home")
@Api(tags = "网站页面信息类模块")
public class HomeController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private HomeService homeService;

    /**
     * 走马灯
     * @return  RespMsg 全类型的返回值  返回Banner的img地址
     */
    @GetMapping("/getCarousel")
    @ApiOperation(value = "获取走马灯的文章列表")
    public RespResult getCarousel() {
        RespResult respResult = articleService.getArticleHasImage();
        return respResult;
    }

    /**
     * 初始化界面
     * @return
     */
    @GetMapping("/init")
    public RespResult init(){
        return RespResult.success("获取成功");
    }
    @GetMapping("/getCards")
    @ApiOperation(value = "获取文章列表")
    public RespResult getCards(HttpServletRequest request, HttpServletResponse httpServletResponse){
        RespResult respResult = articleService.selectArticleByRandom(6);
        return respResult;
    }
    @GetMapping("/announcement")
    @ApiOperation(value = "获取公告")
    public RespResult announcement(){
        RespResult respResult  = homeService.getAnnouncement();
        return respResult;
    }

    @GetMapping("/one")
    @ApiOperation("获取一言")
    public RespResult one(){
        RespResult respResult  = homeService.getOne();
        return respResult;
    }

    @GetMapping("/login")
    @ApiOperation("微信小程序登录")
    public RespResult login(String code){
        RespResult respResult  = homeService.getUserInfo(code);
        return respResult;
    }

    @GetMapping("/user")
    @ApiOperation("微信小程序信息存储")
    public RespResult user( String openid, String info){
        RespResult respResult  = homeService.storageUser(openid,info);
        return respResult;
    }
}
