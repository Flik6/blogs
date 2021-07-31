package com.coco52.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coco52.entity.MyUser;
import com.coco52.entity.Role;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserMapper extends BaseMapper<MyUser> {

}
