package kiyoshi.webtest.hikari.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import kiyoshi.webtest.hikari.common.CONSTANT;
import kiyoshi.webtest.hikari.common.CommonResponse;
import kiyoshi.webtest.hikari.domain.Administration;
import kiyoshi.webtest.hikari.domain.Report;
import kiyoshi.webtest.hikari.domain.Scenario;
import kiyoshi.webtest.hikari.domain.UserInfo;
import kiyoshi.webtest.hikari.persistence.AdministrationMapper;
import kiyoshi.webtest.hikari.persistence.ReportMapper;
import kiyoshi.webtest.hikari.persistence.ScenarioMapper;
import kiyoshi.webtest.hikari.persistence.UserInfoMapper;
import kiyoshi.webtest.hikari.service.AdministrationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Service("administrationService")
@Slf4j
public class AdministrationServiceImpl implements AdministrationService {
    @Autowired
    private AdministrationMapper administrationMapper;

    @Autowired
    private ScenarioMapper scenarioMapper;

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Override
    public CommonResponse<Object> createAdministration(Administration administration, Integer scenarioId) {
        //必填项不可为空
        if(administration.getAdministrationName().isBlank()){
            return CommonResponse.createForError("administration name is required!");
        }
        CommonResponse<Object> checkResult = checkFiled("administration_name",administration.getAdministrationName(),scenarioId);
        if(!checkResult.isSuccess()){
            return checkResult;
        }
        Scenario scenario = scenarioMapper.selectById(scenarioId);
        administration.setUsername(scenario.getUsername());
        administration.setProjectName(scenario.getProjectName());
        administration.setScenarioName(scenario.getScenarioName());
        administration.setTimes(0);
        administration.setState(0);
        administration.setCreateTime(LocalDateTime.now());
        administration.setUpdateTime(LocalDateTime.now());

        int rows = administrationMapper.insert(administration);
        if(rows == 0){
            return CommonResponse.createForError("add administration failed");
        }
        return CommonResponse.createForSuccess();
    }

    @Override
    public CommonResponse<Object> checkFiled(String fieldName, String fieldValue, Integer scenarioId) {
        Scenario scenario = scenarioMapper.selectById(scenarioId);
        if(StringUtils.equals(fieldName,"administration_name")) {
            long rows = administrationMapper.selectCount(Wrappers.<Administration>query().eq("administration_name",fieldValue).eq("scenario_name",scenario.getScenarioName()));
            if (rows > 0) {
                return CommonResponse.createForError("for this user's this project's this scenario, such administration already exists");
            }
        }
        else{
            return CommonResponse.createForError("parameter error");
        }
        return CommonResponse.createForSuccess();
    }

    @Override
    public CommonResponse<Administration> getAdministrationDetail(Integer administrationId) {
        Administration administration = administrationMapper.selectById(administrationId);
        if(administration == null){
            return CommonResponse.createForError("no such administration");
        }
        else {
            return CommonResponse.createForSuccess(administration);
        }
    }

    @Override
    public CommonResponse<Page<Administration>> getAdministrationList(Integer scenarioId, String keyword, String orderBy, int pageNum, int pageSize) {
        QueryWrapper<Administration> queryWrapper = new QueryWrapper<>();
        if(scenarioId != null){
            Scenario scenario = scenarioMapper.selectById(scenarioId);
            queryWrapper.eq("scenario_name",scenario.getScenarioName()).eq("project_name",scenario.getProjectName()).eq("username",scenario.getUsername());
            if(scenario == null ) {
                log.info("for this user's this project, no such scenario");
                return CommonResponse.createForSuccess();
            }
        }

        Page<Administration> result = new Page<>();
        result.setCurrent(pageNum);
        result.setSize(pageSize);

        //增加关键字模糊查询
        if(StringUtils.isNotBlank(keyword)){
            queryWrapper.like("administration_name",keyword);//这里最好使用string-buffer
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

        result = administrationMapper.selectPage(result,queryWrapper);
        result.setCurrent(pageNum);
        result.setSize(pageSize);

        return CommonResponse.createForSuccess(result);
    }

    @Override
    public CommonResponse<Integer> getAdministrationId(Integer scenarioId, String administrationName) {

        if(scenarioId == null){
            return CommonResponse.createForError("scenario id is needed!");
        }
        Scenario scenario = scenarioMapper.selectById(scenarioId);
        if(scenario == null){
            return CommonResponse.createForError("for this user, no such scenario");
        }
        else {
            QueryWrapper<Administration> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("administration_name",administrationName).eq("scenario_name",scenario.getScenarioName()).eq("project_name",scenario.getProjectName()).eq("username",scenario.getUsername());
            Administration administration = administrationMapper.selectOne(queryWrapper);
            if(administration == null ){
                return CommonResponse.createForError("for this user's project's scenario ,such administration does not exist");
            }
            return CommonResponse.createForSuccess(administration.getId());
        }
    }

    @Override
    public CommonResponse<Object> administrate(Integer administrationId, UserInfo loginUser) {

        RestTemplate restTemplate = new RestTemplate();
        Administration administration = administrationMapper.selectById(administrationId);
        Scenario scenario = scenarioMapper.selectOne(Wrappers.<Scenario>query().eq("scenario_name",administration.getScenarioName()).eq("project_name",administration.getProjectName()).eq("username",administration.getUsername()));

        //构造请求url
        StringBuffer url = new StringBuffer();
        url.append(CONSTANT.POST_JENKINS_SERVER).append("generic-webhook-trigger/invoke");
        String postUrl = url.toString();
        //添加请求参数
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(postUrl);
        builder.queryParam("token",loginUser.getJenkinsWorkspace());


       //构造请求体，jenkins解析要求，顺序不可乱
        Map<String,String> map = new HashMap<>();
        map.put("ref",scenario.getUsername());
        map.put("repositoryURL",scenario.getGithubRepositoryUrl());
        map.put("branch",scenario.getBranch());
        map.put("credentialsId",scenario.getCredentialsId());
        map.put("testClass",scenario.getTestClass());

        //构造header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 创建 HttpEntity 包含请求体和请求头
        HttpEntity<Map<String, String>> request = new HttpEntity<>(map, headers);

        // 发送 POST 请求
        ResponseEntity<String> response = restTemplate.postForEntity(builder.toUriString(), request, String.class);

        // 处理响应
        System.out.println("Response: " + response.getBody());
        if(response.getBody() == null){
            return CommonResponse.createForError("administrate failed.");
        }
        else {
            //如果响应了，那么视本次调度成功，用户总调度成功数+1，为本次调度创建报告记录，报告号为用户总调度成功数
            // todo:事务处理

            //用户总调度数+1,更新数据库
            UpdateWrapper<UserInfo> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",loginUser.getId());
            updateWrapper.set("total_administrate_times",loginUser.getTotalAdministrateTimes()+1);
            updateWrapper.set("update_time",LocalDateTime.now());
            userInfoMapper.update(updateWrapper);

            loginUser.setTotalAdministrateTimes(loginUser.getTotalAdministrateTimes()+1);

            //创建本次报告记录
            Report report = new Report();
            StringBuffer reportName = new StringBuffer();
            reportName.append(administration.getAdministrationName()).append("Report").append(loginUser.getTotalAdministrateTimes());
            report.setAdministrationName(administration.getAdministrationName());
            report.setReportName(reportName.toString());
            report.setScenarioName(administration.getScenarioName());
            report.setProjectName(administration.getProjectName());
            report.setUsername(administration.getUsername());
            report.setCreateTime(LocalDateTime.now());
            report.setJenkinsId(loginUser.getTotalAdministrateTimes());
            reportMapper.insert(report);

            return CommonResponse.createForSuccess(response.getBody());
        }

    }
}
