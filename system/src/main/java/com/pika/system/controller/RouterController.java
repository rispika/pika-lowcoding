package com.pika.system.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.pika.common.R;
import com.pika.system.entity.Router;
import com.pika.system.service.RouterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "pika_router表相关接口")
@RestController
@RequestMapping("/router")
public class RouterController {

    @Resource
    private RouterService routerService;

    @ApiOperation(value = "获取其路由信息", notes = "根据权限判断获取其路由信息", tags = "路由操作")
    @GetMapping("/pikaMenuAdmin/{page}/{size}")
    public R pikaMenuAdmin(@ApiParam(value = "当前页", required = true) @PathVariable Long page,
                           @ApiParam(value = "大小", required = true) @PathVariable Long size) {
        // 获取权限
        return routerService.pikaMenuAdmin(page, size);
    }

    @ApiOperation(value = "根据level获取路由信息",tags = "路由操作")
    @GetMapping("/level/{level}")
    public R pikaMenuAdminByParentId(@ApiParam(value = "级别",required = true) @PathVariable Long level) {
        return R.ok().data("route",routerService.lambdaQuery().eq(Router::getLevel, level).list());
    }

    @ApiOperation(value = "根据id获取路由信息",tags = "路由操作")
    @GetMapping("/{id}")
    public R pikaMenuAdminById(@ApiParam(value = "id", required = true) @PathVariable Long id){
        return R.ok().data("route",routerService.getById(id));
    }

    @ApiOperation(value = "添加一个路由信息", tags = "路由操作")
    @PutMapping("/save")
    public R saveRouter(@Validated @RequestBody Router router) {
        routerService.save(router);
        return R.ok();
    }

    @ApiOperation(value = "修改一个路由信息", tags = "路由操作")
    @PostMapping("/update")
    public R updateRouter(@Validated @RequestBody Router router) {
        routerService.updateById(router);
        return R.ok();
    }

    @ApiOperation(value = "根据id删除路由信息",tags = "路由操作")
    @DeleteMapping("/{id}")
    public R delRouter(@ApiParam(value = "id", required = true) @PathVariable Long id) {
        routerService.removeById(id);
        return R.ok();
    }


    @ApiOperation(value = "获取侧边栏动态路由", tags = "路由操作")
    @GetMapping("/pikaMenu")
    public R pikaMenu() {
        return routerService.pikaMenu();
    }

    @ApiOperation(value = "获取动态路由信息", tags = "路由操作")
    @GetMapping("/pikaRouter")
    public R pikaRouter() {
        // 获取权限
        return routerService.pikaRouter();
    }

}
