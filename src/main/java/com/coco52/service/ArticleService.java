package com.coco52.service;

import com.coco52.entity.Article;
import com.coco52.entity.RespResult;

import javax.servlet.http.HttpServletRequest;


public interface ArticleService {
    public RespResult selectArticleByUUID(String uuid);
    RespResult selectArticleByRandom(Integer num);

    RespResult publishAnArticle(Article article,HttpServletRequest request);

    RespResult getArticle(String articleId);

    RespResult getArticleInfo(String articleId);

    RespResult getArticleHasImage();
}
