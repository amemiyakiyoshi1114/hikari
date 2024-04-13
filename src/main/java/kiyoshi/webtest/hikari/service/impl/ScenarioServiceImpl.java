package kiyoshi.webtest.hikari.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import kiyoshi.webtest.hikari.common.CONSTANT;
import kiyoshi.webtest.hikari.common.CommonResponse;
import kiyoshi.webtest.hikari.domain.Project;
import kiyoshi.webtest.hikari.domain.Scenario;
import kiyoshi.webtest.hikari.persistence.ProjectMapper;
import kiyoshi.webtest.hikari.persistence.ScenarioMapper;
import kiyoshi.webtest.hikari.service.ScenarioService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service("scenarioService")
@Slf4j
public class ScenarioServiceImpl implements ScenarioService {
    @Autowired
    private ScenarioMapper scenarioMapper;

    @Autowired
    private ProjectMapper projectMapper;
    @Override
    public CommonResponse<Object> createScenario(Scenario scenario, Integer projectId) {
        //必填项不可为空
        if(scenario.getScenarioName().isBlank()){
            return CommonResponse.createForError("scenario name is required!");
        }
        else if(scenario.getGithubRepositoryUrl().isBlank()){
            return CommonResponse.createForError("your GitHub repository is required!");
        }
        else if(scenario.getBranch().isBlank()){
            return CommonResponse.createForError("your git branch is required!");
        }
        else if(scenario.getCredentialsId().isBlank()){
            return CommonResponse.createForError("your GitHub token is required!");
        }
        else if(scenario.getTestClass().isBlank()){
            return CommonResponse.createForError("your test class is required!");
        }
        else if(projectId == null){
            return CommonResponse.createForError("choose your project");
        }

        CommonResponse<Object> checkResult = checkFiled("scenario_name",scenario.getScenarioName(),projectId);
        if(!checkResult.isSuccess()){
            return checkResult;
        }
        Project project = projectMapper.selectOne(Wrappers.<Project>query().eq("id",projectId));

        scenario.setUsername(project.getUsername());
        scenario.setProjectName(project.getProjectName());
        scenario.setCreateTime(LocalDateTime.now());
        scenario.setUpdateTime(LocalDateTime.now());

        int rows = scenarioMapper.insert(scenario);
        if(rows == 0){
            return CommonResponse.createForError("add scenario failed");
        }
        return CommonResponse.createForSuccess();
    }

    @Override
    public CommonResponse<Object> checkFiled(String fieldName, String fieldValue, Integer projectId) {
        Project project = projectMapper.selectById(projectId);
        if(StringUtils.equals(fieldName,"scenario_name")) {
            long rows = scenarioMapper.selectCount(Wrappers.<Scenario>query().eq("scenario_name",fieldValue).eq("project_name",project.getProjectName()));
            if (rows > 0) {
                return CommonResponse.createForError("for this user's this project, such scenario already exists");
            }
        }
        else{
            return CommonResponse.createForError("parameter error");
        }
        return CommonResponse.createForSuccess();
    }

    @Override
    public CommonResponse<Scenario> getScenarioDetail(Integer scenarioId) {
        Scenario scenario = scenarioMapper.selectById(scenarioId);
        if(scenario == null){
            return CommonResponse.createForError("no such scenario");
        }
        else {
            return CommonResponse.createForSuccess(scenario);
        }
    }

    @Override
    public CommonResponse<Page<Scenario>> getScenarioList(Integer projectId, String keyword, String orderBy, int pageNum, int pageSize) {
        QueryWrapper<Scenario> queryWrapper = new QueryWrapper<>();
        if(projectId != null){
            Project project = projectMapper.selectById(projectId);
            queryWrapper.eq("project_name",project.getProjectName()).eq("username",project.getUsername());
            if(project == null ) {
                log.info("for this user no such project");
                return CommonResponse.createForSuccess();
            }
        }

        Page<Scenario> result = new Page<>();
        result.setCurrent(pageNum);
        result.setSize(pageSize);

        //增加关键字模糊查询
        if(StringUtils.isNotBlank(keyword)){
            queryWrapper.like("scenario_name",keyword);//这里最好使用string-buffer
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

        result = scenarioMapper.selectPage(result,queryWrapper);
        result.setCurrent(pageNum);
        result.setSize(pageSize);

        return CommonResponse.createForSuccess(result);
    }

    @Override
    public CommonResponse<Integer> getScenarioId(Integer projectId, String scenarioName) {
        if(projectId == null){
            return CommonResponse.createForError("project id is needed!");
        }
        Project project = projectMapper.selectById(projectId);
        if(project == null){
            return CommonResponse.createForError("for this user, no such project");
        }
        else {
            QueryWrapper<Scenario> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("scenario_name",scenarioName).eq("project_name",project.getProjectName()).eq("username",project.getUsername());
            Scenario scenario = scenarioMapper.selectOne(queryWrapper);
            if(scenario == null ){
                return CommonResponse.createForError("for this user's project ,such scenario does not exist");
            }
            return CommonResponse.createForSuccess(scenario.getId());
        }
    }
}
