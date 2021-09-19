package com.coco52.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    private Timestamp timestamp = new Timestamp(new Date().getTime());

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("register_time",timestamp,metaObject);
        this.setFieldValByName("update_time",timestamp,metaObject);
        this.setFieldValByName("is_available",true,metaObject);
        this.setFieldValByName("is_expires",false,metaObject);
        this.setFieldValByName("is_lock",false,metaObject);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("update_time",LocalDateTime.now(),metaObject);
    }
}
