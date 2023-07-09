package com.pika.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pika.common.R;
import com.pika.system.constant.RouterLevel;
import com.pika.system.entity.Router;
import com.pika.system.mapper.RouterMapper;
import com.pika.system.service.RouterService;
import com.pika.system.vo.RouterVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RouterServiceImpl extends ServiceImpl<RouterMapper, Router> implements RouterService {
    @Override
    public R pikaMenuAdmin(Long page, Long size) {
        Page<Router> routerPage = page(new Page<>(page, size));
        return R.ok().data(routerPage);
    }

    @Override
    public R pikaMenu() {
        List<Router> routers = list();
        // 排除隐藏菜单
        Map<Long, List<Router>> routerMap = routers.stream()
                .filter(router -> router.getLevel() != RouterLevel.LEVEL_HIDDEN)
                .map(router -> {
                    if (router.getParent() == null) {
                        router.setParent(0L);
                    }
                    return router;
                }).collect(Collectors.groupingBy(Router::getParent));
        List<Router> level1Routers = routerMap.get(0L);
        List<RouterVO> routerVOS = BeanUtil.copyToList(level1Routers, RouterVO.class);
        if (routerVOS != null) {
            for (RouterVO routerVO : routerVOS) {
                routerVO.setChildren(BeanUtil.copyToList(routerMap.get(routerVO.getId()), RouterVO.class));
                if (routerVO.getChildren() != null) {
                    for (RouterVO child : routerVO.getChildren()) {
                        child.setChildren(BeanUtil.copyToList(routerMap.get(child.getId()), RouterVO.class));
                    }
                }
            }
        }

        return R.ok().data("menu", routerVOS);
    }

    @Override
    public R pikaRouter() {
        List<Router> routers = lambdaQuery().select(Router::getTo, Router::getName, Router::getComponentUrl)
                .list();
        List<Map<String, Object>> targetMaps = new ArrayList<>();
        for (Router router : routers) {
            Map<String, Object> targetMap = new HashMap<>();
            targetMap.put("name", router.getName());
            targetMap.put("path", router.getTo());
            targetMap.put("component", router.getComponentUrl());
            targetMaps.add(targetMap);
        }
        return R.ok().data("routers", targetMaps);
    }
}
