package com.pika.system.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * (Permission)表实体类
 *
 * @author makejava
 * @since 2023-07-17 12:43:46
 */
@Data
public class PermissionDTO extends Model<PermissionDTO> {


    private Long roleId;


    @ApiModelProperty("路径正则表达式")
    private String pathPattern;

}

