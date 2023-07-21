package com.pika.common.UserDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * (Role)表实体类
 *
 * @author pika
 * @since 2023-07-18 18:38:05
 */
@Data
public class Role {

    @ApiModelProperty("角色id")
    private Long roleId;

    @ApiModelProperty("角色标题")    
    private String roleName;

    @ApiModelProperty("描述")    
    private String description;

    @ApiModelProperty("创建时间")    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}

