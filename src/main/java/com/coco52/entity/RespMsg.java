package com.coco52.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("通用返回实体类")
public class RespMsg {
    @ApiModelProperty("状态码")
    private Integer code;
    @ApiModelProperty("提示信息")
    private String msg;
    @ApiModelProperty("数据")
    private Object data;


    public static RespMsg success(String msg, Object data) {
        return new RespMsg(200, msg, data);
    }

    public static RespMsg success(String msg) {
        return new RespMsg(200, msg, null);
    }

    public static RespMsg fail(String msg, Object data) {
        return new RespMsg(500, msg, data);
    }

    public static RespMsg fail(String msg) {
        return new RespMsg(500, msg, null);
    }

}
