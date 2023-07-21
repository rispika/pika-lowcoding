package com.pika.system.controller;

import com.pika.common.R;
import com.pika.system.entity.Role;
import com.pika.system.service.RoleService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 角色控制器
 *
 * @author RIS
 * @date 2023/07/17
 */
@Api("角色API控制器")
@RestController
@RequestMapping("role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @PutMapping("save")
    public R saveRole(@RequestBody Role role) {
        if (roleService.save(role)) {
            return R.ok();
        } else {
            return R.fail(500, "添加Role失败!请稍后再试!");
        }
    }

}
