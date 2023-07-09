package com.pika.generate.constant;

import io.swagger.models.auth.In;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public enum GenerateType {
    // 字符
    CHAR("char",0, String.class),
    VARCHAR("varchar",1, String.class),
    BLOB("blob", 2, byte[].class),
    /**
     * text 类型无须指定长度。
     * 若数据库未启用严格的 sqlmode ，当插入的值超过 text 列的最大长度时，则该值会被截断插入并生成警告。
     * text 类型字段不能有默认值。
     * varchar 可直接创建索引，text 字段创建索引要指定前多少个字符。
     * text 类型检索效率比 varchar 要低。
     */
    TEXT("text", 3, String.class),
    // 数值
    FLOAT("float", 4, Float.class),
    DOUBLE("double", 5, Double.class),
    DECIMAL("decimal", 7, BigDecimal.class),
    BIT("bit", 8, Boolean.class),
    TINYINT("tinyint", 9, Integer.class),
    SMALLINT("smallint", 10, Integer.class),
    MEDIUMINT("mediumint", 11, Integer.class),
    INTEGER("integer", 12, Long.class),
    BIGINT("bigint", 13, BigInteger.class),
    // 时(8间
    DATE("date", 14, LocalDateTime.class),
    TIME("time", 15, LocalDateTime.class),
    DATETIME("datetime", 16, LocalDateTime.class),
    TIMESTAMP("timestamp", 17, Timestamp.class);
    private String generateType;

    private Integer generateCode;
    private Class<?> generateTypeClass;

    public Integer getGenerateCode() {
        return generateCode;
    }

    /**
     * 获得实例
     *
     * @param code 代码
     * @return {@link GenerateType}
     */
    public static GenerateType getInstance(Integer code) {
        for (GenerateType generateType : values()) {
            if (Objects.equals(generateType.getGenerateCode(), code)) {
                return generateType;
            }
        }
        throw new RuntimeException("未找到对应枚举类型");
    }

    /**
     * 转为字符串
     *
     * @param code 代码
     * @return {@link String}
     */
    public static String toString(Integer code) {
        return getInstance(code).generateType;
    }


    /**
     * 得到类型类
     *
     * @param code 代码
     * @return {@link Class}<{@link ?}>
     */
    public static Class<?> getTypeClass(Integer code) {
        return getInstance(code).generateTypeClass;
    }

    /**
     * 生成类型
     *
     * @param s      年代
     * @param i      我
     * @param aClass 一个类
     */
    GenerateType(String s, int i, Class<?> aClass) {
        this.generateType = s;
        this.generateCode = i;
        this.generateTypeClass = aClass;
    }


    /**
     * 得到所有类型
     *
     * @return {@link Map}<{@link Integer}, {@link String}>
     */
    public static Map<Integer, String> getAllTypes() {
        Map<Integer, String> types = new HashMap<>();
        for (GenerateType generateType : values()) {
            types.put(generateType.generateCode, generateType.generateType);
        }
        return types;
    }

    /**
     * 得到生成表单类型
     *
     * @param generateCode 生成代码
     * @return {@link String}
     */
    public static String getGenerateFormType(Integer generateCode) {
        GenerateType instance = getInstance(generateCode);
        switch (instance) {
            case BIT:
                return "mapper";
            default:
               return null;
        }
    }
}
