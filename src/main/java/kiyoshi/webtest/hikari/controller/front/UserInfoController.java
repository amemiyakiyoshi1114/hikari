package kiyoshi.webtest.hikari.controller.front;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import kiyoshi.webtest.hikari.common.CONSTANT;
import kiyoshi.webtest.hikari.common.CommonResponse;
import kiyoshi.webtest.hikari.domain.UserInfo;
import kiyoshi.webtest.hikari.dto.UpdateUserInfoDTO;
import kiyoshi.webtest.hikari.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userInfo")
@Validated//我们对参数进行校验采用validation时需要在使用的类上写上这个注解，不然是不会起效的
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("login")
    public CommonResponse<UserInfo> login(@RequestParam @NotBlank(message = "username can not be null") String username,
                                          @RequestParam @NotBlank(message = "password can not be null") String password,
                                          HttpSession session) {
//        System.out.println(username + password);
        CommonResponse<UserInfo> result = userInfoService.login(username, password);
        if (result.isSuccess()) {
            session.setAttribute(CONSTANT.LOGIN_USER, result.getData());
        }
        return result;
    }

    //用户参数校验
    @PostMapping("check_field")
    public CommonResponse<Object> checkField(
            @RequestParam @NotBlank(message = "field name can not be null") String fieldName,
            @RequestParam @NotBlank(message = "field value can not be null") String fieldValue
    ) {
        return userInfoService.checkFiled(fieldName, fieldValue);
    }

    //spring MVC 取值是通过RequestBody
    //requesBody 和@valid注解完成参数校验
    //MD5->使用第三方组件时：三种方法：1手动实现；2用spring的用法；3springboot的用法
    @PostMapping("register")
    public CommonResponse<Object> register(
            @RequestBody @Valid UserInfo userInfo
    ) {
        return userInfoService.register(userInfo);
    }

    @PostMapping("get_user_detail")
    public CommonResponse<UserInfo> getUserDetail(HttpSession session){
        UserInfo loginUser = (UserInfo)session.getAttribute(CONSTANT.LOGIN_USER);
        if(loginUser == null){
            return CommonResponse.createForError("user need login");
        }
        return userInfoService.getUserDetail(loginUser.getId());
    }


    @PostMapping("update_user_info")
    public CommonResponse<Object> updateUserInfo(
            @RequestBody @Valid UpdateUserInfoDTO updateUser,
            HttpSession session
    ){
        UserInfo loginUser = (UserInfo) session.getAttribute(CONSTANT.LOGIN_USER);
        if(loginUser == null){
            return CommonResponse.createForError("user need login");
        }
        loginUser.setJenkinsWorkspace(updateUser.getJenkinsWorkspace());

        CommonResponse<Object> result = userInfoService.updateUserInfo(loginUser);
        if(result.isSuccess()){
            loginUser = userInfoService.getUserDetail(loginUser.getId()).getData();
            session.setAttribute(CONSTANT.LOGIN_USER,loginUser);
            return CommonResponse.createForSuccess();
        }
        return CommonResponse.createForError(result.getMessage());
    }

    @GetMapping("logout")
    public CommonResponse<Object> logout(HttpSession session){
        session.removeAttribute(CONSTANT.LOGIN_USER);
        return CommonResponse.createForSuccess("log out success");
    }

}
