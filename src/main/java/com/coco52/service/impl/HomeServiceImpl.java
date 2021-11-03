package com.coco52.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coco52.entity.BlogInfo;
import com.coco52.entity.RespResult;
import com.coco52.entity.WechatUserInfo;
import com.coco52.mapper.blog.BlogInfoMapper;
import com.coco52.mapper.blog.WechatUserInfoMapper;
import com.coco52.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@DS("master")
public class HomeServiceImpl implements HomeService {

    @Autowired
    private BlogInfoMapper blogInfoMapper;

    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    private WechatUserInfoMapper wechatUserInfoMapper;

    @Override
    public RespResult getAnnouncement() {
        QueryWrapper<BlogInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("name","value").eq("name","announcement");
        BlogInfo blogInfo = blogInfoMapper.selectOne(queryWrapper);
        return RespResult.success("公告获取成功！",blogInfo);
    }

    @Override
    public RespResult getOne() {
        ResponseEntity<String> forEntity = restTemplate.getForEntity("https://v1.hitokoto.cn/?encode=json", String.class);
        if (forEntity.getStatusCodeValue()==200){
            String one = forEntity.getBody();
            return RespResult.success("一言获取成功！",one);
        }

        return RespResult.fail("一言获取失败!");
    }

    @Override
    public RespResult getUserInfo(String code) {
        String url="https://api.weixin.qq.com/sns/jscode2session?appid=wx66662520ef711f7f&secret=f39882f43d6b7b5a6a3d2730cff2c9a4&js_code="+code+"&grant_type=authorization_code";
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        if (forEntity.getStatusCodeValue()==200){
            JSONObject jsonObject = JSONObject.parseObject(forEntity.getBody());
            System.out.println("jsonObject.toString() = " + jsonObject.toJSONString());
            if (jsonObject.containsKey("errcode")){
                int errcode = (int)jsonObject.get("errcode");
                switch (errcode){
                    case 0:return RespResult.fail("获取用户成功！",jsonObject.toJSONString());
                    case -1:return RespResult.fail("系统繁忙，此时请开发者稍候再试！",jsonObject.toJSONString());
                    case 40029:return RespResult.fail("code 无效！",jsonObject.toJSONString());
                    case 45011:return RespResult.fail("频率限制，每个用户每分钟100次！",jsonObject.toJSONString());
                    case 40226:return RespResult.fail("高风险等级用户，小程序登录拦截 ！",jsonObject.toJSONString());
                    default:return RespResult.fail("未知原因，请联系管理员！",jsonObject.toJSONString());
                }
            }else {
                return RespResult.success("获取用户成功！",jsonObject.toJSONString());
            }
        }

        return RespResult.fail("获取用户信息失败！",null);

    }

    @Override
    public RespResult storageUser(String openid,String userInfo) {
        System.out.println("userInfo = " + userInfo);
        JSONObject jsonObject = JSONObject.parseObject(userInfo);
//        {"userInfo":{"nickName":"Flik","gender":1,"language":"zh_CN","city":"","province":"","country":"Bermuda","avatarUrl":"https://thirdwx.qlogo.cn/mmopen/vi_32/PiajxSqBRaEKqjssYibAcCdD0gApzzItlkXXEK19IB1HqBWhAj8IJMuKU3qaOQNKdQehhicrrw5wgntIXJTNz8FSA/132"}}
        QueryWrapper<WechatUserInfo> wechatUserInfoQueryWrapper = new QueryWrapper<>();
        wechatUserInfoQueryWrapper.select("openid").eq("openid",openid);
        WechatUserInfo wechatUserInfo = wechatUserInfoMapper.selectOne(wechatUserInfoQueryWrapper);
        if (!ObjectUtils.isEmpty(wechatUserInfo)){
            return RespResult.success("用户无需操作！",null);
        }
        System.out.println("jsonObjectInfo.toString() = " + jsonObject.toString());
        WechatUserInfo info = jsonObject.toJavaObject(WechatUserInfo.class);
        info.setOpenid(openid);
        info.setRegisterTime(LocalDateTime.now());
        int insert = wechatUserInfoMapper.insert(info);
        return insert==1?RespResult.success("用户添加成功"):RespResult.fail("用户添加失败！");
    }


}
