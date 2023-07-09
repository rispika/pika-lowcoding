package com.pika.common.exception;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.pika.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@ControllerAdvice
@ResponseBody
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public R exceptionHandle(Exception e) {
        String date = LocalDateTimeUtil.formatNormal(LocalDateTime.now());
        e.printStackTrace();
        log.error("\n服务器发生了错误:{},\n" +
                        "发生时间:{}",
                e.getMessage(),
                date);
        return R.fail(500, e.getMessage());
    }

}
