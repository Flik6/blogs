package com.coco52.service;

import com.coco52.entity.RespResult;

public interface WordService {
    RespResult getBookList();

    RespResult getWordsList(Integer current, String bookId);

    RespResult GetWordInfo(String wordId, String options);
}
