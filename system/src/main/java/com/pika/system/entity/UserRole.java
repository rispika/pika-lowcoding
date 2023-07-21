package com.pika.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;

/**
 * (UserRole)表实体类
 *
 * @author pika
 * @since 2023-07-18 18:30:52
 */
@Data
public class UserRole {

    @ApiModelProperty("用户id")        
    private Long userId;

    @ApiModelProperty("角色id")        
    private Long roleId;

}

