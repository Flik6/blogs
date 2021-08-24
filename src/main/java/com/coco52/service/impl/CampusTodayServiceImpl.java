package com.coco52.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.coco52.entity.RespMsg;
import com.coco52.entity.SchoolUser;
import com.coco52.mapper.SchoolUserMapper;
import com.coco52.service.CampusTodayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class CampusTodayServiceImpl implements CampusTodayService {

    @Autowired
    private SchoolUserMapper schoolUserMapper;
    @Override
    public RespMsg storageUser(SchoolUser user) {
        QueryWrapper<SchoolUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",user.getUsername());
        SchoolUser schoolUser = schoolUserMapper.selectOne(queryWrapper);
        if (StringUtils.isEmpty(schoolUser)){
            user.setCreateTime(new Timestamp(new Date().getTime()));
            System.out.println(user);
            int insert = schoolUserMapper.insert(user);
            return insert==1?RespMsg.success("任务创建成功！"):RespMsg.fail("任务创建失败！请检查信息是否填写错误");
        }
        UpdateWrapper<SchoolUser> userUpdateWrapper= new UpdateWrapper<>();
        userUpdateWrapper.eq("username",user.getUsername());
        userUpdateWrapper.eq("email",user.getEmail());
        int update = schoolUserMapper.update(user, userUpdateWrapper);
        return update==1?RespMsg.success("此账号已存在数据库中，密码、邮箱校验成功，已成功修改~"):RespMsg.fail("修改失败，用户名、邮箱与数据库中不同！~");
    }
}
