package com.coco52.controller;

import com.coco52.entity.Article;
import com.coco52.entity.RespResult;
import com.coco52.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Api(tags = "文章模块")
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @ApiOperation("发表文章")
    @PreAuthorize("hasAnyAuthority('admin','secretary','user')")
    @PostMapping("/articles")
    public RespResult publishAnArticle(@RequestBody Article article,HttpServletRequest request){
        RespResult respResult=articleService.publishAnArticle(article,request);
        return respResult;
    }

    @ApiOperation(value = "查询文章",notes = "根据文章id查询文章信息")
    @GetMapping("/articles/{articleId}")
    public RespResult getArticle(@PathVariable("articleId") String articleId){
        RespResult respResult=articleService.getArticle(articleId);
        return respResult;
    }

    @ApiOperation(value = "查询文章详细信息",notes = "根据文章id查询文章详细信息")
    @PreAuthorize("hasAnyAuthority('admin','secretary','user')")
    @GetMapping("/info/{articleId}")
    public RespResult getArticleInfo(@PathVariable("articleId") String articleId){
        RespResult respResult=articleService.getArticleInfo(articleId);
        return respResult;
    }
}
