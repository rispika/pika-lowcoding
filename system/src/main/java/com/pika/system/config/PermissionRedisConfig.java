package com.pika.system.config;

import com.pika.common.constant.RedisConstant;
import com.pika.system.entity.Permission;
import com.pika.system.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class PermissionRedisConfig {

    @Resource
    private PermissionService permissionService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void initPermission() {
        log.info("start to initPermission!");
        Map<String, List<Permission>> listMap = permissionService.getGroupByRoleName();
        if (!Objects.isNull(listMap)) {
            listMap.forEach((k, list) -> {
                for (Permission permission : list) {
                    redisTemplate.opsForSet().add(RedisConstant.PERMISSION_PREFIX + k, permission.getPathPattern());
                }
            });
        }

    }

}
