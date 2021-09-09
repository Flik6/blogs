package com.coco52.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coco52.entity.Role;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select role_name,role_id id from role r, role_account ra where r.id=ra.roleId and ra.user_uuid=#{uuid}")
    List<Role> selectRolesByUserUuid(String uuid);
}
