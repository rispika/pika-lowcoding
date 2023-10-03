package com.pika.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * (PikaRouter)表实体类
 *
 * @author pika
 * @since 2023-07-03 09:57:10
 */
@Data
@ApiModel("(PikaRouter)表实体类")
public class Router {
    @ApiModelProperty("ID")
    @TableId(type = IdType.AUTO)
    private Long id;
    @ApiModelProperty(value = "索引(例1-1,2-4)", required = true)
    @TableField("`label`")
    @NotNull
    private String label;
    @ApiModelProperty(value = "路由名称", required = true)
    @TableField("`name`")
    @NotNull
    private String name;
    @ApiModelProperty(value = "路由跳转路径")
    @TableField("`path`")
    @NotNull
    private String path;
    @ApiModelProperty("路由icon")
    private String icon;
    @ApiModelProperty("父路由id")
    private Long parent;
    @ApiModelProperty("层数 1 一级菜单 2 三级菜单 0 -1 -2同理的无孩子菜单 -3隐藏菜单")
    @Max(2)
    @Min(-3)
    private Integer level;
    @ApiModelProperty("路由组件路径")
    private String componentUrl;

}

