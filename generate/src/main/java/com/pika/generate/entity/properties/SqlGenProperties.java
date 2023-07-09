package com.pika.generate.entity.properties;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.pika.generate.constant.GenerateType;
import com.pika.generate.entity.GenerateCol;
import com.pika.generate.entity.GenerateTable;
import lombok.*;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Data
@ToString
@EqualsAndHashCode
public class SqlGenProperties {
    private String tableName;
    private String tableComment;
    private final Set<SqlColAttribute> cols = new LinkedHashSet<>();
    private final Set<String> primaryKeys = new LinkedHashSet<>();
    // 生成时间
    private final String generateTime = LocalDateTimeUtil.formatNormal(LocalDateTime.now());;

    public SqlGenProperties(GenerateTable generateTable, List<GenerateCol> generateCols) {
        this.tableName = generateTable.getTableName();
        this.tableComment = generateTable.getTableComment();
        addCols(generateCols);
    }

    public void addCols(List<GenerateCol> cols) {
        for (GenerateCol col : cols) {
            SqlColAttribute sqlColAttribute = new SqlColAttribute();
            sqlColAttribute.setColName(col.getColName());
            sqlColAttribute.setColType(GenerateType.toString(col.getColType()));
            sqlColAttribute.setColLength(col.getColLength());
            sqlColAttribute.setIsPrimary(col.getIsPrimary());
            sqlColAttribute.setIsAutoIncrement(col.getIsAutoIncrement());
            sqlColAttribute.setNullable(col.getNullable());
            sqlColAttribute.setDefaultValue(col.getDefaultValue());
            sqlColAttribute.setColComment(col.getColComment());
            if (sqlColAttribute.getIsPrimary()) {
                this.primaryKeys.add(sqlColAttribute.getColName());
            }
            this.cols.add(sqlColAttribute);
        }
    }

    public void addCol(GenerateCol col) {
        SqlColAttribute sqlColAttribute = new SqlColAttribute();
        sqlColAttribute.setColName(col.getColName());
        sqlColAttribute.setColType(GenerateType.toString(col.getColType()));
        sqlColAttribute.setColLength(col.getColLength());
        sqlColAttribute.setIsPrimary(col.getIsPrimary());
        sqlColAttribute.setIsAutoIncrement(col.getIsAutoIncrement());
        sqlColAttribute.setNullable(col.getNullable());
        sqlColAttribute.setDefaultValue(col.getDefaultValue());
        sqlColAttribute.setColComment(col.getColComment());
        cols.add(sqlColAttribute);
        if (sqlColAttribute.getIsPrimary()) {
            primaryKeys.add(sqlColAttribute.getColName());
        }
    }

    @Data
    public static class SqlColAttribute {

        private String colName;
        private String colType;
        private Integer colLength;
        private Boolean isPrimary;
        private Boolean isAutoIncrement;
        private Boolean nullable;
        private String defaultValue;
        private String colComment;

    }
}
