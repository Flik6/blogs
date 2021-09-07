package com.coco52.service;

import com.coco52.entity.RespMsg;

import javax.servlet.http.HttpServletRequest;

public interface UtilService {
    RespMsg sign(String url, String userId, HttpServletRequest request);
}
