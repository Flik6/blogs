package com.coco52.mapper.blog;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coco52.entity.Account;
import com.coco52.entity.Permissions;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionsMapper extends BaseMapper<Permissions> {
    @Select("select t1.*,p.permName, permTag, url\n" +
            "from ((select a.*, ra.roleId from (account a, role_account ra) where a.uuid = ra.userUuid and a.uuid=#{uuid}) t1,\n" +
            "     role_permission rp,permissions p)\n" +
            "where t1.roleId = rp.roleId and rp.permissionId=p.id;")
    List<Permissions> selectPermissionByAccount(Account account);


}
