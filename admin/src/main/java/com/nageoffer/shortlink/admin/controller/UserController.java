package com.nageoffer.shortlink.admin.controller;

import com.nageoffer.shortlink.admin.common.convention.result.Result;
import com.nageoffer.shortlink.admin.common.convention.result.Results;
import com.nageoffer.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.nageoffer.shortlink.admin.dto.resp.UserRespDTO;
import com.nageoffer.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理 控制层
 * controller 里面不能写 业务代码
 */
@RestController // 相当于是 restbody 以及 controller 两个接口的一个结合
@RequiredArgsConstructor // 使用构造器的方式进行注入
public class UserController {

    public final UserService userService;


    // 相当于 RequestMapping 默认值是get
    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    @GetMapping("/api/linkservice/v1/user/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String username){
        UserRespDTO result = userService.getUserByUsername(username);
        if (result == null){
            return new Result<UserRespDTO>().setCode(UserErrorCodeEnum.USER_NULL.code()).setMessage(UserErrorCodeEnum.USER_NULL.message());
        }else {
            return Results.success(result);
        }
    }
}







