package org.galsang.controller;

import org.galsang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户前端控制器
 *
 * @author tengpeng.gao
 * @since 2018/9/5
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/default")
    public String addUser1(@Validated UserBean userBean, BindingResult bindingResult) {
        String errorInfo = this.handlerBindingResult(bindingResult);
        if (errorInfo != null) {
            return errorInfo;
        }
        return userService.addUserByDefaultTransaction(userBean) ? "SUCCESS" : "FAIL";
    }

    @PostMapping("/2")
    public String addUser2(@Validated UserBean userBean, BindingResult bindingResult) {
        String errorInfo = this.handlerBindingResult(bindingResult);
        if (errorInfo != null) {
            return errorInfo;
        }
        return userService.addUser(userBean) ? "SUCCESS" : "FAIL";
    }


    /**
     * 处理参数校验失败信息
     *
     * @param bindingResult 绑定结果
     * @return 验证失败信息，如果为null 说明验证通过
     */
    private String handlerBindingResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<ObjectError> errors = bindingResult.getAllErrors();
            for (ObjectError err : errors) {
                sb.append(err.getDefaultMessage() + ";  ");
            }
            return "参数不正确 --->" + sb;
        }
        return null;
    }

}
