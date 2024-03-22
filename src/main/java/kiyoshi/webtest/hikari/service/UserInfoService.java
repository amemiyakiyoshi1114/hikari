package kiyoshi.webtest.hikari.service;

import kiyoshi.webtest.hikari.common.CommonResponse;
import kiyoshi.webtest.hikari.domain.UserInfo;

import java.net.InterfaceAddress;

public interface UserInfoService {
    CommonResponse<UserInfo> login(String username, String password);//用户登录

    CommonResponse<Object> checkFiled(String fieldName, String fieldValue);//用户参数校验

    CommonResponse<Object> register(UserInfo userInfo);//用户注册

   // CommonResponse<String> getForgetQuestion(String username);

    //CommonResponse<String> checkForgetAnswer(String username,String question,String answer);

    //CommonResponse<Object> resetForgetPassword(String username,String newPassword,String forgetToken);

    CommonResponse<UserInfo> getUserDetail(Integer userId);

    //CommonResponse<Object> resetPassword(String oldPassword,String newPassword, User user);

    CommonResponse<Object> updateUserInfo(UserInfo userInfo);

}
