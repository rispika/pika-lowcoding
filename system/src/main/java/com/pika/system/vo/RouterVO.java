package com.pika.system.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class RouterVO {
    @ApiModelProperty("ID")
    private Long id;
    @ApiModelProperty(value = "索引(例1-1,2-4)",required = true)
    @TableField("`index`")
    private String index;
    @ApiModelProperty(value = "路由名称",required = true)
    @TableField("`name`")
    private String name;
    @ApiModelProperty(value = "路由跳转路径")
    @TableField("`to`")
    private String to;
    @ApiModelProperty("路由icon")
    private String icon;
    @ApiModelProperty("父路由id")
    @TableField(exist = false)
    private List<RouterVO> children;
}
