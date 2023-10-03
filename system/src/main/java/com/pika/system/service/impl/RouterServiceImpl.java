package com.pika.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pika.common.R;
import com.pika.common.constant.RedisConstant;
import com.pika.common.utils.JwtUtil;
import com.pika.system.constant.RouterLevel;
import com.pika.system.entity.Router;
import com.pika.system.mapper.RouterMapper;
import com.pika.system.service.RouterService;
import com.pika.system.dto.RouterDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class RouterServiceImpl extends ServiceImpl<RouterMapper, Router> implements RouterService {
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Override
    public R pikaMenuAdmin(Long page, Long size, String routerName, Integer routerLevel) {
        LambdaQueryWrapper<Router> wrapper = new LambdaQueryWrapper<>();
        if (!StrUtil.isBlank(routerName)) {
            wrapper.like(Router::getName, routerName);
        }
        if (!Objects.isNull(routerLevel)) {
            wrapper.eq(Router::getLevel, routerLevel);
        }
        Page<Router> routerPage = page(new Page<>(page, size), wrapper);
        return R.ok().data(routerPage);
    }

    @Override
    public R pikaMenu(HttpServletRequest request) {
        String token = request.getHeader("authorization");
        Map<String, Object> tokenInfo = JwtUtil.getTokenInfo(token);
        String role = (String) tokenInfo.get("role");
        Set<Object> members = redisTemplate.opsForSet().members(RedisConstant.PERMISSION_PREFIX + role);
        if (members == null) {
            return R.fail("用户权限异常!");
        }
        List<Router> routers = list();
        // 权限过滤
        routers = routers.stream().filter(router -> {
            for (Object member : members) {
                if (Pattern.matches((String) member, router.getPath())){
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toList());
        // 排除隐藏菜单
        // 菜单
        Map<Long, List<Router>> routerMap = routers.stream()
                .filter(router -> router.getLevel() != RouterLevel.LEVEL_HIDDEN)
                .map(router -> {
                    if (router.getParent() == null) {
                        router.setParent(0L);
                    }
                    return router;
                }).collect(Collectors.groupingBy(Router::getParent));
        List<Router> level1Routers = routerMap.get(0L);
        List<RouterDTO> routerDTOS = BeanUtil.copyToList(level1Routers, RouterDTO.class);
        if (routerDTOS != null) {
            for (RouterDTO routerDTO : routerDTOS) {
                routerDTO.setChildren(BeanUtil.copyToList(routerMap.get(routerDTO.getId()), RouterDTO.class));
                if (routerDTO.getChildren() != null) {
                    for (RouterDTO child : routerDTO.getChildren()) {
                        child.setChildren(BeanUtil.copyToList(routerMap.get(child.getId()), RouterDTO.class));
                    }
                }
            }
        }
        // 路由菜单
        List<Map<String, Object>> targetMaps = new ArrayList<>();
        for (Router router : routers) {
            Map<String, Object> targetMap = new HashMap<>();
            targetMap.put("label", router.getLabel());
            targetMap.put("name", router.getName());
            targetMap.put("path", router.getPath());
            targetMap.put("component", router.getComponentUrl());
            targetMaps.add(targetMap);
        }
        return R.ok().data("menu", routerDTOS).data("routers", targetMaps);
    }

    @Override
    public R pikaRouter() {
        List<Router> routers = lambdaQuery()
                .list();
        List<Map<String, Object>> targetMaps = new ArrayList<>();
        for (Router router : routers) {
            Map<String, Object> targetMap = new HashMap<>();
            targetMap.put("label", router.getLabel());
            targetMap.put("name", router.getName());
            targetMap.put("path", router.getPath());
            targetMap.put("component", router.getComponentUrl());
            targetMaps.add(targetMap);
        }
        return R.ok().data("routers", targetMaps);
    }
}
