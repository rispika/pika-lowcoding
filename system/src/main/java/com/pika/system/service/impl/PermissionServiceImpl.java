package com.pika.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.pika.system.entity.Role;
import com.pika.system.entity.RolePermission;
import com.pika.system.mapper.PermissionMapper;
import com.pika.system.entity.Permission;
import com.pika.system.mapper.RoleMapper;
import com.pika.system.mapper.RolePermissionMapper;
import com.pika.system.service.PermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * (Permission)表服务实现类
 *
 * @author makejava
 * @since 2023-07-17 12:43:47
 */
@Service("permissionService")
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Resource
    private RolePermissionMapper rolePermissionMapper;
    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<Permission> getListByRoleId(Long roleId) {
        MPJLambdaWrapper<RolePermission> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(Permission.class)
                .leftJoin(Permission.class, Permission::getPermissionId, RolePermission::getPermissionId)
                .eq(RolePermission::getRoleId, roleId);
        return rolePermissionMapper.selectJoinList(Permission.class, wrapper);
    }

    @Override
    public Map<String, List<Permission>> getGroupByRoleName() {
        MPJLambdaWrapper<RolePermission> wrapper = new MPJLambdaWrapper<>();
        wrapper
                .selectAll(Permission.class)
                .select(Role::getRoleName)
                .leftJoin(Role.class, Role::getRoleId, RolePermission::getRoleId)
                .leftJoin(Permission.class, Permission::getPermissionId, RolePermission::getPermissionId);
        List<Permission> permissionList = rolePermissionMapper.selectJoinList(Permission.class, wrapper);
        Map<String, List<Permission>> map = Optional.ofNullable(permissionList)
                .map(list -> list.stream().collect(Collectors.groupingBy(Permission::getRoleName)))
                .get();
        return map;
    }
}

