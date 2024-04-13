package kiyoshi.webtest.hikari.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import kiyoshi.webtest.hikari.common.CONSTANT;
import kiyoshi.webtest.hikari.common.CommonResponse;
import kiyoshi.webtest.hikari.domain.*;
import kiyoshi.webtest.hikari.persistence.AdministrationMapper;
import kiyoshi.webtest.hikari.persistence.ReportMapper;
import kiyoshi.webtest.hikari.service.ReportService;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("reportService")
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private AdministrationMapper administrationMapper;

    @Override
    public CommonResponse<Page<Report>> getReportList(Integer administrationId, String keyword, String orderBy, int pageNum, int pageSize) {
        QueryWrapper<Report> queryWrapper = new QueryWrapper<>();
        if(administrationId != null){
            Administration administration = administrationMapper.selectById(administrationId);
            queryWrapper.eq("administration_name",administration.getAdministrationName()).eq("scenario_name",administration.getScenarioName()).eq("project_name",administration.getProjectName()).eq("username",administration.getUsername());
            if(administration == null ) {
                log.info("for this user no such administration");
                return CommonResponse.createForSuccess();
            }
        }

        Page<Report> result = new Page<>();
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

        result = reportMapper.selectPage(result,queryWrapper);
        result.setCurrent(pageNum);
        result.setSize(pageSize);

        return CommonResponse.createForSuccess(result);
    }

    @Override
    public CommonResponse<Integer> getReportId(Integer administrationId, String reportName) {
        if(administrationId == null){
            return CommonResponse.createForError("administration id is needed!");
        }
        Administration administration = administrationMapper.selectById(administrationId);
        if(administration == null){
            return CommonResponse.createForError("for this user's this scenario, no such administration.");
        }
        else {
            QueryWrapper<Report> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("report_name",reportName).eq("administration_name",administration.getAdministrationName()).eq("scenario_name",administration.getScenarioName()).eq("project_name",administration.getProjectName()).eq("username",administration.getUsername());
            Report report = reportMapper.selectOne(queryWrapper);
            if(report == null ){
                return CommonResponse.createForError("no such report, have you administrated?");
            }
            return CommonResponse.createForSuccess(report.getId());
        }
    }

    @Override
    public CommonResponse<Object> getReportDetail(Integer reportId, UserInfo loginUser) {
        Report report = reportMapper.selectById(reportId);
        if(report == null){
            return CommonResponse.createForError("no such report");
        }
        else {
           //GGG System.out.println(report);
           //向jenkins拼凑get请求
            StringBuffer getUrl = new StringBuffer();
            getUrl.append(CONSTANT.POST_JENKINS_SERVER).append("job/").append(loginUser.getJenkinsWorkspace()).append("/").append(report.getJenkinsId()).append("/api/json");
            System.out.println("---------------------------------");
            System.out.println(getUrl);
            System.out.println("----------------------------------");
            RestTemplate restTemplate = new RestTemplate();
            // 3.使用RestTemplate发起请求与接收返回值
            String resultData = restTemplate.getForObject(getUrl.toString(), String.class);
            //System.out.println("从服务端返回结果: " + resultData);
//            JsonObject reportJson = JsonParser.parseString(resultData).getAsJsonObject();
//            System.out.println(reportJson);
            //String url = reportJson.getAsJsonObject("data").get("url").getAsString();
            //System.out.println("url" + url);
            Gson gson = new Gson();
            JenkinsReportJson jenkinsJob = gson.fromJson(resultData, JenkinsReportJson.class);
            System.out.println(jenkinsJob.getUrl());
            return CommonResponse.createForSuccess(jenkinsJob.getUrl());
        }
    }
}
