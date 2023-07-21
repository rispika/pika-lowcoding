package com.pika.generate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pika.common.R;
import com.pika.generate.entity.GenerateTable;
import org.springframework.web.multipart.MultipartFile;

/**
 * (GenerateTable)表服务接口
 *
 * @author pika
 * @since 2023-07-05 12:16:21
 */
public interface GenerateTableService extends IService<GenerateTable> {
    R generateCode(Long tableId);

    R generateCodeAndLoad(Long tableId, String pkg);

    R queryTemplateInfo(Long tableId);

    R createTableBySqlFile(MultipartFile file);
}

