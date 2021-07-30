package com.coco52.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coco52.entity.Role;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {

    @Select("select roleName,roleId id from role r, role_account ra where r.id=ra.roleId and ra.userUuid=#{uuid}")
    List<Role> selectRolesByUserUuid(String uuid);
}
