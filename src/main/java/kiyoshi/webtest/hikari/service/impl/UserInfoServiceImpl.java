package kiyoshi.webtest.hikari.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.benmanes.caffeine.cache.Cache;
import jakarta.annotation.Resource;
import kiyoshi.webtest.hikari.common.CommonResponse;
import kiyoshi.webtest.hikari.domain.UserInfo;
import kiyoshi.webtest.hikari.persistence.UserInfoMapper;
import kiyoshi.webtest.hikari.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.baomidou.mybatisplus.core.toolkit.Wrappers.query;

@Service("userInfoService")//未来容器创建这个对象并注入时，更快的创建出来
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    //这里报错是java编译期和运行时的问题，因为userMapper是个接口，我们借助mybatisplus在运行时帮我们new出来
    private UserInfoMapper userInfoMapper;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Resource
    private Cache<String, String> localCache;
    @Override
    public CommonResponse<UserInfo> login(String username, String password) {
        UserInfo loginUser = userInfoMapper.selectOne(Wrappers.<UserInfo>query().eq("username",username));
        if(loginUser == null){
            return CommonResponse.createForError("maybe something wrong with your username");
        }
        boolean checkPassword = bCryptPasswordEncoder.matches(password,loginUser.getPassword());
        loginUser.setPassword(StringUtils.EMPTY);
        return checkPassword?CommonResponse.createForSuccess(loginUser):CommonResponse.createForError("maybe something wrong with your password");
    }

    @Override
    public CommonResponse<Object> checkFiled(String fieldName, String fieldValue){
        if(StringUtils.equals(fieldName,"username")){
            long rows = userInfoMapper.selectCount(Wrappers.<UserInfo>query().eq("username",fieldValue));
            if(rows > 0){
                return CommonResponse.createForError("such username already exists");
            }
        }
        else if(StringUtils.equals(fieldName,"Jenkins_workspace")) {
            long rows = userInfoMapper.selectCount(Wrappers.<UserInfo>query().eq("Jenkins_workspace",fieldValue));
            if (rows > 0) {
                return CommonResponse.createForError("such phone number already exists");
            }
        }
        else{
            return CommonResponse.createForError("parameter error");
        }
        return CommonResponse.createForSuccess();
    }


    @Override
    public CommonResponse<Object> register(UserInfo userInfo) {
        CommonResponse<Object> checkResult = checkFiled("username",userInfo.getUsername());
        if(!checkResult.isSuccess()){
            return checkResult;
        }

        userInfo.setPassword(bCryptPasswordEncoder.encode(userInfo.getPassword()));

        userInfo.setCreateTime(LocalDateTime.now());
        userInfo.setUpdateTime(LocalDateTime.now());

        int rows = userInfoMapper.insert(userInfo);
        if(rows == 0){
            return CommonResponse.createForError("register failed");
        }
        return CommonResponse.createForSuccess();
    }

    @Override
    public CommonResponse<UserInfo> getUserDetail(Integer userId) {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        if(userInfo == null){
            return CommonResponse.createForError("can not find current user's information");
        }
        userInfo.setPassword(StringUtils.EMPTY);
        return CommonResponse.createForSuccess(userInfo);
    }

    @Override
    public CommonResponse<Object> updateUserInfo(UserInfo userInfo) {
        userInfo.setUpdateTime(LocalDateTime.now());
        UpdateWrapper<UserInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",userInfo.getId());
        updateWrapper.set("Jenkins_workspace",userInfo.getJenkinsWorkspace());
        updateWrapper.set("update_time",userInfo.getUpdateTime());
        long rows = userInfoMapper.update(userInfo,updateWrapper);

        if(rows > 0){
            return CommonResponse.createForSuccess();
        }
        return CommonResponse.createForError("update user info failed");
    }

}

