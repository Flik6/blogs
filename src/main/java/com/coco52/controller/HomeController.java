package com.coco52.controller;

import com.coco52.entity.RespResult;
import com.coco52.service.ArticleService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 走马灯
     * @return  RespMsg 全类型的返回值  返回Banner的img地址
     */
    @GetMapping("/getCarousel")
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
    public RespResult getCards(HttpServletRequest request, HttpServletResponse httpServletResponse){
        RespResult respResult = articleService.selectArticleByRandom(6);
        return respResult;
    }
}
