package com.coco52.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coco52.entity.RespResult;
import com.coco52.entity.words.*;
import com.coco52.enums.ResultCode;
import com.coco52.mapper.words.*;
import com.coco52.service.WordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/word")
@Api(tags = "单词类模块")
public class WordController {

    @Autowired
    private WordService wordService;
    /**
     * 获取book列表
     * 用于初始化小程序首页可供学习单词接口
     *
     * @return
     */
    @GetMapping("/book")
    @ApiOperation("获取book列表")
    public RespResult getBookList() {
        RespResult result  = wordService.getBookList();
        return result;
    }

    @GetMapping("/words/{bookId}/{page}")
    @ApiOperation("根据bookId 获取单词")
    public RespResult getWordsList(@PathVariable("bookId") String bookId,@PathVariable("page") Integer current) {

        RespResult result  = wordService.getWordsList(current, bookId);
        return result;
    }

    @GetMapping("/wordInfo/{id}/{options}")
    @ApiOperation("根据wordId获取单词相关信息")
    public RespResult GetWordInfo(@PathVariable("id") String wordId,@PathVariable("options") String options) {
        RespResult result  = wordService.GetWordInfo(wordId, options);
        return result;
    }


}
