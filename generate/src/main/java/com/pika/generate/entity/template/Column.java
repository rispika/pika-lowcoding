package com.pika.generate.entity.template;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 列实体
 *
 * @author RIS
 * @date 2023/07/09
 */
@Data
public class Column {

    /**
     * 表列标签
     */
    private String tableColumnLabel;
    /**
     * 表列属性名
     */
    private String tableColumnProp;
    /**
     * 表列类型(mapper -> 选择器)
     */
    private String tableColumnType;
    /**
     * 映射器
     */
    private List<Mapper> mappers;
    /**
     * 是否展示搜索框
     */
    private Boolean searchFlag;
    /**
     * 表列规则
     */
    private List<Rule> tableColumnRules;


    public <T> void addMapper(String mapperLabel, T mapperValue) {
        if (Objects.isNull(mappers)) {
            mappers = new ArrayList<>();
        }
        Mapper<Object> mapper = Mapper.builder().mapperLabel(mapperLabel).mapperValue(mapperValue).build();
        mappers.add(mapper);
    }

    public void addTableColumnRules(Rule rule) {
        if (Objects.isNull(tableColumnRules)) {
            tableColumnRules = new ArrayList<>();
        }
        this.tableColumnRules.add(rule);
    }


}
