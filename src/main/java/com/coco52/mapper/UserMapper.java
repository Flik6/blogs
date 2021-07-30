package com.coco52.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coco52.entity.MyUser;
import com.coco52.entity.Role;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper extends BaseMapper<MyUser> {

}
