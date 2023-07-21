package com.pika.system.controller;

import com.pika.common.R;
import com.pika.system.service.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("permission")
@Api(tags = "权限相关API")
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    @GetMapping("/getGroupByRoleId")
    @ApiOperation("按RoleId分组获取权限信息")
    public R getGroupByRoleId() {
        return R.ok().data(permissionService.getGroupByRoleName());
    }

    @ApiOperation("根据RoleId获取权限信息")
    @GetMapping("/getListByRoleId/{roleId}")
    public R getListByRoleId(@PathVariable Long roleId) {
        return R.ok().data(permissionService.getListByRoleId(roleId));
    }

}
