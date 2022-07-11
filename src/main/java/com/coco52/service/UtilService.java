package com.coco52.service;

import com.coco52.entity.RespResult;

import javax.servlet.http.HttpServletRequest;

public interface UtilService {
    RespResult sign(String url, String userId, HttpServletRequest request);
    RespResult parseJson(String jsons);

}
