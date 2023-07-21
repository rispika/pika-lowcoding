package com.pika.system.controller;

import com.pika.common.R;
import com.pika.common.constant.ResMsg;
import com.pika.system.dto.UserQueryDTO;
import com.pika.system.entity.User;
import com.pika.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(tags = "用户API控制器")
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @ApiOperation("根据用户名获取用户信息")
    @GetMapping("/getByUsername/{username}")
    public R getByUsername(@PathVariable("username") String username) {
        return R.ok().data(userService.getByUsername(username));
    }

    @ApiOperation("注册账号")
    @PostMapping("/register")
    public R register(@RequestBody User user) {
        // 加密密码
        String unencryptedPwd = user.getPassword();
        String encodedPwd = "{bcrypt}" + passwordEncoder.encode(unencryptedPwd);
        user.setPassword(encodedPwd);
        boolean save = userService.save(user);
        if (save) {
            return R.ok();
        } else {
            return R.fail(500, "注册失败");
        }
    }

    /**
     * 分页查询用户信息
     *
     * @param page         页面
     * @param size         大小
     * @param userQueryDTO 用户查询dto
     * @return {@link R}
     */
    @ApiOperation("分页查询用户信息")
    @GetMapping("/queryUserPages/{page}/{size}")
    public R queryUserPages(@ApiParam(value = "页码",required = true) @PathVariable("page") Long page,
                            @ApiParam(value = "大小",required = true) @PathVariable("size") Long size,
                            @ApiParam(value = "搜索DTO") UserQueryDTO userQueryDTO) {
        return R.ok().data(userService.queryUserPages(page, size, userQueryDTO));
    }

    /**
     * 删除用户
     *
     * @param userId 用户id
     * @return {@link R}
     */
    @ApiOperation("删除用户")
    @DeleteMapping("/removeUser/{userId}")
    public R removeUser(@ApiParam(value = "用户id", required = true) @PathVariable("userId") Long userId) {
        if (userService.removeById(userId)) {
            return R.ok(ResMsg.DELETE_OK);
        } else {
            return R.fail(ResMsg.DELETE_FAIL);
        }
    }

    /**
     * 根据用户id获取用户信息
     *
     * @param userId 用户id
     * @return {@link R}
     */
    @ApiOperation("根据用户id获取用户信息")
    @GetMapping("/getUserById/{userId}")
    public R getUserById(@ApiParam(value = "用户id", required = true) @PathVariable("userId") Long userId) {
        User user = userService.getById(userId);
        return R.ok().data("user", user);
    }

    /**
     * 根据用户id更新数据
     *
     * @param user 用户
     * @return {@link R}
     */
    @ApiOperation("根据用户id更新数据")
    @PostMapping("/updateByUserId")
    public R updateByUserId(@ApiParam(value = "用户信息",required = true) @RequestBody User user) {
        if (userService.updateById(user)) {
            return R.ok(ResMsg.UPDATE_OK);
        } else {
            return R.fail(ResMsg.UPDATE_FAIL);
        }
    }

    /**
     * 保存用户
     *
     * @param user 用户
     * @return {@link R}
     */
    @ApiOperation("保存用户")
    @PutMapping("/saveUser")
    public R saveUser(@ApiParam(value = "用户信息",required = true) @RequestBody User user) {
        if (userService.save(user)) {
            return R.ok(ResMsg.INSERT_OK);
        } else {
            return R.fail(ResMsg.INSERT_FAIL);
        }
    }


}
