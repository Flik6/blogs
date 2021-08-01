package com.coco52.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("registerTime",new Date(),metaObject);
        this.setFieldValByName("updateTime",new Date(),metaObject);
        this.strictInsertFill(metaObject,"isAvailable",Integer.class,1);
        this.strictInsertFill(metaObject,"isExpires",Integer.class,0);
        this.strictInsertFill(metaObject,"isLock",Integer.class,0);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("update_time",LocalDateTime.now(),metaObject);
    }
}
