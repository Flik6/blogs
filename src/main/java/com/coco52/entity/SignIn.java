package com.coco52.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("亚滴-打卡")
public class SignIn {
    @ApiModelProperty(value = "打卡所需的url",required = true)
    private String url;
    @ApiModelProperty(value = "员工id-需抓包获取",required = true)
    private String userId;
}
