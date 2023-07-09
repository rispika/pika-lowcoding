package com.pika.generate.entity;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import cn.hutool.core.date.DatePattern;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

/**
 * (Test)表实体类
 *
 * @author pika
 * @since 2023-07-08 12:26:39
 */
@ApiModel(value = "test", description = "这是一个test表")
@TableName("test")
@Data
public class Test extends Model<Test> {

    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty("主键id")
    private Long id;
    @TableField(value = "context")
    @ApiModelProperty("内容!!!!!!!!!")
    private String context;
    @TableField(value = "buer")
    private Boolean buer;
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