package com.coco52.entity;

import com.coco52.enums.ResultCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("通用返回实体类")
public class RespResult {
    @ApiModelProperty("状态码")
    private Integer code;
    @ApiModelProperty("提示信息")
    private String msg;
    @ApiModelProperty("数据")
    private Object data;

    public static RespResult success(String msg) {
        return new RespResult(200, msg, null);
    }
    public static RespResult success(String msg, Object data) {
        return new RespResult(200, msg, data);
    }
    public static RespResult success(Integer code, String msg, Object data) {
        return new RespResult(code, msg, data);
    }
    public static RespResult success(ResultCode resultCode, Object data) {
        return new RespResult(resultCode.getCode(), resultCode.getMessage(), data);
    }


    public static RespResult fail(String msg) {
        return new RespResult(500, msg, null);
    }
    public static RespResult fail(String msg, Object data) {
        return new RespResult(500, msg, data);
    }
    public static RespResult fail(Integer code, String msg, Object data) {
        return new RespResult(code, msg, data);
    }
    public static RespResult fail(ResultCode resultCode, Object data) {
        return new RespResult(resultCode.getCode(), resultCode.getMessage(), data);
    }


}
