package com.pika.generate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pika.common.R;
import com.pika.generate.entity.GenerateCol;
import com.pika.generate.mapper.GenerateColMapper;
import com.pika.generate.service.GenerateColService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (PikaGenerateCol)表服务实现类
 *
 * @author makejava
 * @since 2023-07-05 10:01:29
 */
@Service
public class GenerateColServiceImpl extends ServiceImpl<GenerateColMapper, GenerateCol> implements GenerateColService {

    @Override
    public R queryAllTablesInfo() {
        List<String> tables = baseMapper.queryAllTablesInfo();
        return R.ok().data("tables",tables);
    }
}

