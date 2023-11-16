package com.nageoffer.shortlink.admin.controller;

import com.nageoffer.shortlink.admin.common.convention.result.Result;
import com.nageoffer.shortlink.admin.common.convention.result.Results;
import com.nageoffer.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.nageoffer.shortlink.admin.dto.req.UserLoginReqDTO;
import com.nageoffer.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.nageoffer.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.nageoffer.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.nageoffer.shortlink.admin.dto.resp.UserRespDTO;
import com.nageoffer.shortlink.admin.service.UserService;
import jakarta.servlet.http.PushBuilder;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     *
     * @param username
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/user/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String username) {
        UserRespDTO result = userService.getUserByUsername(username);

        return Results.success(result);
//        return result == null ? Results.failure(UserErrorCodeEnum.USER_EXIST) : Results.success(result);

    }

    /**
     * 查询用户名是否存在
     *
     * @param username
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/user/has-username")
    public Result<Boolean> hasUsername(@RequestParam("username") String username) {
        return Results.success(userService.hasUsername(username));
    }

    /**
     * 注册用户
     *
     * @param requestParam
     * @return
     */
    @PostMapping("api/short-link/admin/v1/user")
    public Result<Void> register(@RequestBody UserRegisterReqDTO requestParam) {
        userService.register(requestParam);
        return Results.success();
    }


    /**
     * 修改用户
     *
     * @param requestParam
     * @return
     */
    @PutMapping("/api/short-link/admin/v1/user")
    public Result<Void> update(@RequestBody UserUpdateReqDTO requestParam) {
        userService.update(requestParam);
        return Results.success();
    }

    /**
     * 查找所有用户主要用于调试
     *
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/getAllUser")
    public Result<List<UserRespDTO>> getAllUser() {
        return Results.success(userService.getAllUser());
    }


    /**
     * 用户登录
     *
     * @param requestParam
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/user/login")
    public Result<UserLoginRespDTO> login(@RequestBody UserLoginReqDTO requestParam) {
        return Results.success(userService.login(requestParam));
    }

    /**
     * 检查用户是否登录
     *
     * @param token
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/user/check-login")
    public Result<Boolean> checkLogin(@RequestParam("username") String username, @RequestParam("token") String token) {
        return Results.success(userService.checkLogin(username, token));
    }

    /**
     * 用户退出登录
     * @param username
     * @return
     */
    @DeleteMapping("/api/short-link/admin/v1/user/logout")
    public Result<Void> logout(@RequestParam("username") String username,@RequestParam("token") String token){
        userService.logout(username,token);
        return Results.success();
    }
}







