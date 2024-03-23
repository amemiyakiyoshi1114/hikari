package kiyoshi.webtest.hikari.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import kiyoshi.webtest.hikari.common.CONSTANT;
import kiyoshi.webtest.hikari.common.CommonResponse;
import kiyoshi.webtest.hikari.domain.Project;
import kiyoshi.webtest.hikari.domain.UserInfo;
import kiyoshi.webtest.hikari.persistence.ProjectMapper;
import kiyoshi.webtest.hikari.persistence.UserInfoMapper;
import kiyoshi.webtest.hikari.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service("projectService")
@Slf4j
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public CommonResponse<Object> createProject(Project project,UserInfo loginUser) {
        //必填项不可为空
        if(project.getProjectName().isBlank()){
            return CommonResponse.createForError("project name is required!");
        }

        CommonResponse<Object> checkResult = checkFiled("project_name",project.getProjectName(),loginUser.getUsername());
        if(!checkResult.isSuccess()){
            return checkResult;
        }

        project.setUsername(loginUser.getUsername());
        project.setCreateTime(LocalDateTime.now());
        project.setUpdateTime(LocalDateTime.now());

        int rows = projectMapper.insert(project);
        if(rows == 0){
            return CommonResponse.createForError("add project failed");
        }
        return CommonResponse.createForSuccess();
    }
    @Override
    public CommonResponse<Object> checkFiled(String fieldName, String fieldValue, String loginUser){

         if(StringUtils.equals(fieldName,"project_name")) {
            long rows = projectMapper.selectCount(Wrappers.<Project>query().eq("project_name",fieldValue).eq("username",loginUser));
            if (rows > 0) {
                return CommonResponse.createForError("for this user, such project already exists");
            }
        }
        else{
            return CommonResponse.createForError("parameter error");
        }
        return CommonResponse.createForSuccess();
    }


    @Override
    public CommonResponse<Project> getProjectDetail(Integer projectId) {
        Project project = projectMapper.selectById(projectId);
        if(project == null){
            return CommonResponse.createForError("no such project");
        }
        return CommonResponse.createForSuccess(project);
    }

    @Override
    public CommonResponse<Page<Project>> getProjectList(Integer userId, String keyword, String orderBy, int pageNum, int pageSize) {
        QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
        if(userId != null){
            UserInfo userInfo = userInfoMapper.selectById(userId);
            queryWrapper.eq("username",userInfo.getUsername());
            if(userInfo == null ) {
                log.info("you need create your account first");
                return CommonResponse.createForSuccess();
            }
        }

        Page<Project> result = new Page<>();
        result.setCurrent(pageNum);
        result.setSize(pageSize);

        //增加关键字模糊查询
        if(StringUtils.isNotBlank(keyword)){
            queryWrapper.like("project_name",keyword);//这里最好使用string-buffer
        }

        //增加排序，需要跟前端约定好，什么样的字符串表示什么样的排序，
        if(StringUtils.isNotBlank(orderBy)){
            if(StringUtils.equals(orderBy, CONSTANT.ORDER_BY_CREATE_TIME)){
                queryWrapper.orderByAsc("create_time");
            }
            else if(StringUtils.equals(orderBy,CONSTANT.ORDER_BY_UPDATE_TIME)){
                queryWrapper.orderByDesc("update_time");
            }
        }

        result = projectMapper.selectPage(result,queryWrapper);
        result.setCurrent(pageNum);
        result.setSize(pageSize);

        return CommonResponse.createForSuccess(result);
    }

    @Override
    public CommonResponse<Integer> getProjectId(Integer userId,String projectName){
        if(userId == null){
            return CommonResponse.createForError("user id is needed!");
        }
        UserInfo userInfo = userInfoMapper.selectById(userId);
        if(userInfo == null){
            return CommonResponse.createForError("no such user");
        }
        else {
            String userName = userInfo.getUsername();
            QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username",userName).eq("project_name",projectName);
            //queryWrapper.select("username",userName,"project_name",projectName);
            Project project = projectMapper.selectOne(queryWrapper);
            if(project == null ){
                return CommonResponse.createForError("for this user that project does not exist");
            }
            return CommonResponse.createForSuccess(project.getId());
        }
    }

}
