package com.coco52.entity.VO;

import com.coco52.entity.MyUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespMsg {
    private Integer code;
    private String msg;
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
