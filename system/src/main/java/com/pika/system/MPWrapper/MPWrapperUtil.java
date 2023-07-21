package com.pika.system.MPWrapper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class MPWrapperUtil {

    static {
        Set<MPWrapperType> wrapperTypeSet = MPWrapperType.keySet();
    }

    /**
     * bean包装
     *
     * @param bean 豆
     * @return {@link QueryWrapper}<{@link T}>
     */
    public static <T> QueryWrapper<T> beanToWrapper(Object bean, Class<T> entityClass) {
        if (bean == null) {
            return null;
        }
        MPWrapperType defaultType = MPWrapperType.EQ;
        boolean ignoreNull = true;
        Class<?> aClass = bean.getClass();
        if (aClass.isAnnotationPresent(MPWrapper.class)) {
            MPWrapper wrapperAnnotation = aClass.getAnnotation(MPWrapper.class);
            defaultType = wrapperAnnotation.value();
            ignoreNull = wrapperAnnotation.ignoreNull();
        }
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        try {
            for (Field field : aClass.getDeclaredFields()) {
                field.setAccessible(true);
                Object fieldVal = field.get(bean);
                if (!ignoreNull || fieldVal == null) {
                    continue;
                }
                String colum_name = StrUtil.toUnderlineCase(field.getName());
                if (field.isAnnotationPresent(MPEq.class)) {
                    queryWrapper.eq(colum_name, fieldVal);
                } else if (field.isAnnotationPresent(MPLike.class)) {
                    queryWrapper.like(colum_name, fieldVal);
                } else if (field.isAnnotationPresent(MPIgnore.class)) {
                    continue;
                } else {
                    switch (defaultType) {
                        case EQ:
                            queryWrapper.eq(colum_name, fieldVal);
                            break;
                        case LIKE:
                            queryWrapper.like(colum_name, fieldVal);
                            break;
                    }
                }
            }
            return queryWrapper;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
