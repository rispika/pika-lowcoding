package com.pika.system.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class RouterDTO {
    @ApiModelProperty("ID")
    private Long id;
    @ApiModelProperty(value = "索引(例1-1,2-4)",required = true)
    @TableField("`label`")
    private String label;
    @ApiModelProperty(value = "路由名称",required = true)
    @TableField("`name`")
    private String name;
    @ApiModelProperty(value = "路由跳转路径")
    @TableField("`path`")
    private String path;
    @ApiModelProperty("路由icon")
    private String icon;
    @ApiModelProperty("父路由id")
    @TableField(exist = false)
    private List<RouterDTO> children;
}
