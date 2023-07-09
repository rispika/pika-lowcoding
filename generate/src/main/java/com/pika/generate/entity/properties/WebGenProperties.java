package com.pika.generate.entity.properties;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.pika.generate.constant.GenerateType;
import com.pika.generate.entity.GenerateCol;
import com.pika.generate.entity.GenerateTable;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.*;

@Data
public class WebGenProperties {
    // 包名
    private final String pkg;
    // 类名
    private final String entityName;
    // 小写类名
    private final String name;
    // 属性集合  需要改写 equals hash 保证名字可不重复 类型可重复
    private final Set<Field> fields = new LinkedHashSet<>();
    // 导入类的不重复集合
    private final Set<String> imports = new LinkedHashSet<>();
    private final Set<String> entityAnnotations = new LinkedHashSet<>();
    // 生成时间
    private final String generateTime = LocalDateTimeUtil.formatNormal(LocalDateTime.now());;
    // 解决前端页面渲染问题
    private final String page = "${page}";
    private final String size = "${size}";
    private final String id = "${id}";


    public WebGenProperties(GenerateTable generateTable, String pkg) {
        this.name = StrUtil.toCamelCase(generateTable.getTableName());
        this.entityName = StrUtil.upperFirst(this.name);
        this.pkg = pkg;
        entityAnnotations.add("@ApiModel(value = \"" + generateTable.getTableName() + "\", description = \"" + generateTable.getTableComment() +"\")");
        entityAnnotations.add("@TableName(\"" + generateTable.getTableName() + "\")");
        preImport();
    }


    /**
     * 预导入包
     */
    public void preImport() {
        this.imports.add("java.time.LocalDateTime");
    }

    /**
     * 批量添加字段
     *
     * @param generateCols 生成报表列数
     */
    public void addFields(List<GenerateCol> generateCols) {
        for (GenerateCol generateCol : generateCols) {
            addField(generateCol);
        }
    }

    /**
     * 添加字段
     *
     * @param generateCol 生成坳
     */
    public void addField(GenerateCol generateCol) {
        Field field = new Field();
        Class<?> typeClass = GenerateType.getTypeClass(generateCol.getColType());
        String typeClassName = typeClass.getName();
        field.setFieldType(typeClassName.substring(typeClassName.lastIndexOf(".") + 1));
        field.setFieldName(StrUtil.toCamelCase(generateCol.getColName()));
        field.setSwaggerComment(generateCol.getColComment());
        Set<String> fieldAnnotations = new LinkedHashSet<>();
        if (generateCol.getIsPrimary() && generateCol.getIsAutoIncrement()) {
            // 主键
            fieldAnnotations.add(AnnotationConstant.TABLE_ID(generateCol.getColName()));
        } else {
            if (generateCol.getNullable()) {
                fieldAnnotations.add(AnnotationConstant.TABLE_FIELD_FILL(generateCol.getColName()));
            } else {
                fieldAnnotations.add(AnnotationConstant.TABLE_FIELD(generateCol.getColName()));
            }
        }
        field.setFieldAnnotations(fieldAnnotations);
        fields.add(field);
    }

    public static class AnnotationConstant {
        public static final String DATE_TIME_FORMAT = "@DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)";
        public static final String JSON_FORMAT = "@JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)";
        public static final String FILL_INSERT_UPDATE = "@TableField(fill = FieldFill.INSERT_UPDATE)";
        public static final String FILL_INSERT = "@TableField(fill = FieldFill.INSERT)";

        public static String TABLE_ID(String colName) {
            return "@TableId(value = \"" + colName + "\",type = IdType.AUTO)";
        }

        public static String TABLE_FIELD_FILL(String colName) {
            return "@TableField(value = \"" + colName + "\", fill = FieldFill.UPDATE)";
        }

        public static String TABLE_FIELD(String colName) {
            return "@TableField(value = \"" + colName + "\")";
        }
    }


    /**
     * 成员属性封装对象.
     */
    @Data
    public static class Field {
        // 成员属性类型
        private String fieldType;
        // 成员属性名称
        private String fieldName;
        // 成员注解设定
        private Set<String> fieldAnnotations;
        // 成员注解
        private String swaggerComment;

        /**
         * 一个类的成员属性 一个名称只能出现一次
         * 我们可以通过覆写equals hash 方法 然后放入Set
         *
         * @param o 另一个成员属性
         * @return 比较结果
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Field field = (Field) o;
            return Objects.equals(fieldName, field.fieldName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fieldType, fieldName);
        }
    }

}