package com.coco52.mapper.blog;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coco52.entity.Account;
import com.coco52.entity.Permissions;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountMapper extends BaseMapper<Account> {



}