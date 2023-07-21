package com.pika.common.UserDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserRoleDTO {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码(加密)")
    private String password;

    @ApiModelProperty("匿名")
    private String nickname;

    @ApiModelProperty("角色集合")
    private List<Role> roleList;

    /**
     * 用户角色dto
     *
     * @param user     用户
     * @param roleList 角色列表
     */
    public UserRoleDTO(User user, List<Role> roleList) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.nickname = user.getNickname();
        this.roleList = roleList;
    }
}
