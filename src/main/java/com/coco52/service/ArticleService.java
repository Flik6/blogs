package com.coco52.service;

import com.coco52.entity.RespResult;

public interface ArticleService {
    public RespResult selectArticleByUUID(String uuid);
    RespResult selectArticleByRandom(Integer num);
}
