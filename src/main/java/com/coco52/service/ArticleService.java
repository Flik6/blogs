package com.coco52.service;

import com.coco52.entity.RespMsg;
import org.springframework.stereotype.Service;

public interface ArticleService {
    public RespMsg selectArticleByUUID(String uuid);
    RespMsg selectArticleByRandom(Integer num);
}
