package com.nageoffer.shortlink.admin.dto.req;

import lombok.Data;

/**
 * 用户注册请求参数
 */
@Data
public class UserRegisterReqDTO {
    /**
     * ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;


    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String mail;
}
