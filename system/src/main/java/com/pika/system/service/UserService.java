package com.pika.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pika.system.dto.UserQueryDTO;
import com.pika.system.entity.User;
import com.pika.system.dto.UserRoleDTO;

/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2023-07-17 12:43:43
 */
public interface UserService extends IService<User> {

    UserRoleDTO getByUsername(String username);

    Page<UserQueryDTO> queryUserPages(Long page, Long size, UserQueryDTO userQueryDTO);
}

