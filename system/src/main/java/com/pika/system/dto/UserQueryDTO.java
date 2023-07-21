package com.pika.system.dto;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pika.system.MPWrapper.MPEq;
import com.pika.system.MPWrapper.MPIgnore;
import com.pika.system.MPWrapper.MPWrapper;
import com.pika.system.MPWrapper.MPWrapperType;
import com.pika.system.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Objects;

/**
 * 查询用户dto
 *
 * @author RIS
 * @date 2023/07/19
 */
@Data
@MPWrapper(MPWrapperType.LIKE)
public class UserQueryDTO {
    @ApiModelProperty("用户id")
    @MPEq
    private Long userId;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("匿名")
    private String nickname;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("微信号")
    private String weixin;

    @ApiModelProperty("个性签名")
    private String signature;

    @ApiModelProperty("头像url")
    @MPIgnore
    private String avatar;

    public UserQueryDTO(User user) {
        if (user==null) {
            return;
        }
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.weixin = user.getWeixin();
        this.signature = user.getSignature();
        this.avatar = user.getAvatar();
    }

    public LambdaQueryWrapper<User> lambdaQueryWrapper() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(!Objects.isNull(userId), User::getUserId, userId)
                .like(!StrUtil.isBlank(username), User::getUsername, username)
                .like(!StrUtil.isBlank(nickname), User::getNickname, nickname)
                .like(!StrUtil.isBlank(email), User::getEmail, email)
                ;
        return wrapper;
    }
}
