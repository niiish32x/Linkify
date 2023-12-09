package Linkify.shortlink.admin.dto.req;

import lombok.Data;

@Data
public class UserUpdateReqDTO {
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
