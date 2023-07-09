package com.pika.generate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pika.common.R;
import com.pika.generate.entity.GenerateCol;

/**
 * (PikaGenerateCol)表服务接口
 *
 * @author makejava
 * @since 2023-07-05 10:01:29
 */
public interface GenerateColService extends IService<GenerateCol> {

    R queryAllTablesInfo();

}

