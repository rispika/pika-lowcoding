package com.pika.common;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.db.Page;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class R {

    private Integer code;
    private String msg;
    private Map<String, Object> data = new HashMap<>();

    public static R ok(Integer code, String msg) {
        R r = new R();
        r.code = code;
        r.msg = msg;
        return r;
    }

    public static R ok(Integer code) {
        R r = new R();
        r.code = code;
        return r;
    }

    public static R ok() {
        return R.ok(200);
    }

    public R data(String key, Object data) {
        this.data.put(key, data);
        return this;
    }
    public R data(Map<String, Object> map) {
        this.data = map;
        return this;
    }
    public R data(Object obj) {
        this.data = BeanUtil.beanToMap(obj);
        return this;
    }

    public static R fail(Integer code, String msg) {
        R r = new R();
        r.code = code;
        r.msg = msg;
        return r;
    }

}
