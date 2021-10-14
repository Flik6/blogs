package com.coco52.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("微信小程序用户信息")
public class WechatUserInfo {
    private Long id;
    @ApiModelProperty("小程序 用户唯一号")
    private String openid;
    @ApiModelProperty("昵称")
    private String nickname;
    @ApiModelProperty("性别")
    private int gender;
    @ApiModelProperty("语言")
    private String language;
    @ApiModelProperty("城市")
    private String city;
    @ApiModelProperty("省份")
    private String province;
    @ApiModelProperty("国家")
    private String country;
    @ApiModelProperty("头像地址")
    private String avatarUrl;

    @ApiModelProperty("注册时间")
    private LocalDateTime registerTime;
}
