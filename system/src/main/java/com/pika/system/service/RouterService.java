package com.pika.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pika.common.R;
import com.pika.system.entity.Router;

import javax.servlet.http.HttpServletRequest;

public interface RouterService extends IService<Router> {
    R pikaMenuAdmin(Long page, Long size, String routerName, Integer routerLevel);

    R pikaMenu(HttpServletRequest request);

    R pikaRouter();
}
