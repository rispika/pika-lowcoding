package com.pika.generate.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.pika.generate.entity.GenerateCol;

import java.util.List;

/**
 * (PikaGenerateCol)表数据库访问层
 *
 * @author makejava
 * @since 2023-07-05 10:01:27
 */
public interface GenerateColMapper extends MPJBaseMapper<GenerateCol> {

    List<String> queryAllTablesInfo();
}

