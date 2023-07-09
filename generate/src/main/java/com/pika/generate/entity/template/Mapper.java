package com.pika.generate.entity.template;

import lombok.Builder;
import lombok.Data;

/**
 * 映射器实体
 *
 * @author RIS
 * @date 2023/07/09
 */
@Data
@Builder
public class Mapper<T> {

    private String mapperLabel;
    private T mapperValue;

}
