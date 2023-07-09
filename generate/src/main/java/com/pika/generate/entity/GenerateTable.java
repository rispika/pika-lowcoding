package com.pika.generate.entity;


import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (GenerateTable)表实体类
 *
 * @author pika
 * @since 2023-07-05 12:16:21
 */
@Data
@ApiModel("自动生成代码表")
public class GenerateTable extends Model<GenerateTable> {

    @ApiModelProperty("sql表id")
    @TableId(type = IdType.AUTO)
    private Long tableId;
    @ApiModelProperty("sql表名")
    private String tableName;
    @ApiModelProperty("sql表注释")
    private String tableComment;
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

