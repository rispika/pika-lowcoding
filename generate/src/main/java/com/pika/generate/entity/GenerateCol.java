package com.pika.generate.entity;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * (PikaGenerateCol)表实体类
 *
 * @author pika
 * @since 2023-07-05 10:01:28
 */
@Data
@ApiModel(value = "自动生成代码列", description = "啊啊啊啊")
public class GenerateCol extends Model<GenerateCol> {
    @ApiModelProperty("列id")
    @TableId(type = IdType.AUTO)
    private Long colId;
    @ApiModelProperty("名")
    private String colName;
    @ApiModelProperty("类型")
    private Integer colType;
    @ApiModelProperty("长度")
    @TableField(fill = FieldFill.UPDATE)
    private Integer colLength;
    @ApiModelProperty("是否为主键 0:不是 1:是")
    private Boolean isPrimary;
    @ApiModelProperty("是否自增 0:不是 1:是")
    private Boolean isAutoIncrement;
    @ApiModelProperty("是否可为null 0:不是 1:是")
    private Boolean nullable;
    @ApiModelProperty("默认值")
    @TableField(fill = FieldFill.UPDATE)
    private String defaultValue;
    @ApiModelProperty("列注释")
    @TableField(fill = FieldFill.UPDATE)
    private String colComment;
    @ApiModelProperty("对应生成表的id")
    private Long tableId;
    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @ApiModelProperty("修改时间")
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}

