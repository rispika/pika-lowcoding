package com.pika.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pika.system.entity.Permission;

import java.util.List;
import java.util.Map;

/**
 * (Permission)表服务接口
 *
 * @author makejava
 * @since 2023-07-17 12:43:46
 */
public interface PermissionService extends IService<Permission> {

    List<Permission> getListByRoleId(Long roleId);

    Map<String, List<Permission>> getGroupByRoleName();
}

