package com.pika.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pika.web.entity.Test;
import com.pika.web.mapper.TestMapper;
import com.pika.web.service.TestService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* (Test)表服务实现类
*
* @author pika
* @since 2023-07-09 11:33:18
*/
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements TestService {


}