package com.pika.generate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pika.common.R;
import com.pika.generate.entity.GenerateTable;

/**
 * (GenerateTable)表服务接口
 *
 * @author makejava
 * @since 2023-07-05 12:16:21
 */
public interface GenerateTableService extends IService<GenerateTable> {
    R generateCode(Long tableId);

    R generateCodeAndLoad(Long tableId, String pkg);

    R queryTemplateInfo(Long tableId);
}

