package cn.iocoder.yudao.module.system.api.permission;

import cn.iocoder.yudao.module.system.service.permission.RoleService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * 角色 API 实现类
 *
 * @author 芋道源码
 */
@DubboService
public class RoleApiImpl implements RoleApi {

    @Resource
    private RoleService roleService;

    @Override
    public void validRoleList(Collection<Long> ids) {
        roleService.validateRoleList(ids);
    }
}
