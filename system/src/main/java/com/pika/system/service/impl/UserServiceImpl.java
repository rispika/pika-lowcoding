package com.pika.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.pika.system.MPWrapper.MPWrapperUtil;
import com.pika.system.dto.UserQueryDTO;
import com.pika.system.entity.Role;
import com.pika.system.entity.UserRole;
import com.pika.system.mapper.UserMapper;
import com.pika.system.entity.User;
import com.pika.system.mapper.UserRoleMapper;
import com.pika.system.service.UserService;
import com.pika.system.dto.UserRoleDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2023-07-17 12:43:43
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * 根据用户名获取UserRoleDTO
     *
     * @param username 用户名
     * @return {@link UserRoleDTO}
     */
    @Override
    public UserRoleDTO getByUsername(String username) {
        User user = lambdaQuery().eq(User::getUsername, username)
                .one();
        if (Objects.isNull(user)) {
            return null;
        }
        Long userId = user.getUserId();
        MPJLambdaWrapper<UserRole> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(Role.class)
                .leftJoin(Role.class, Role::getRoleId, UserRole::getRoleId)
                .eq(UserRole::getUserId, userId);
        List<Role> roleList = userRoleMapper.selectJoinList(Role.class, wrapper);
        return new UserRoleDTO(user, roleList);
    }

    /**
     * 分页查询用户信息
     *
     * @param page         页面
     * @param size         大小
     * @param userQueryDTO 用户查询dto
     * @return {@link UserQueryDTO}
     */
    @Override
    public Page<UserQueryDTO> queryUserPages(Long page, Long size, UserQueryDTO userQueryDTO) {
        QueryWrapper<User> wrapper = MPWrapperUtil.beanToWrapper(userQueryDTO, User.class);
        Page<User> userPage = page(new Page<>(page, size), wrapper);
        List<UserQueryDTO> queryDTOS = userPage.getRecords().stream()
                .map(UserQueryDTO::new)
                .collect(Collectors.toList());
        Page<UserQueryDTO> queryPages = new Page<>(page, size, userPage.getTotal());
        queryPages.setRecords(queryDTOS);
        return queryPages;
    }
}

