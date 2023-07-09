package com.pika.generate.entity.template;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 表实体
 *
 * @author RIS
 * @date 2023/07/09
 */
@Data
@Builder
public class Table {

    /**
     * 表列
     */
    List<Column> tableColumns;


}
