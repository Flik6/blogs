package com.coco52.service;

import com.coco52.entity.RespResult;

public interface HomeService {

    RespResult getAnnouncement();

    RespResult getOne();

    RespResult getUserInfo(String code);

    RespResult storageUser(String openid,String userInfo);
}
