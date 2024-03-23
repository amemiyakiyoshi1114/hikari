package kiyoshi.webtest.hikari.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import kiyoshi.webtest.hikari.common.CommonResponse;
import kiyoshi.webtest.hikari.domain.Project;
import kiyoshi.webtest.hikari.domain.Scenario;
import kiyoshi.webtest.hikari.domain.UserInfo;

public interface ScenarioService {
    CommonResponse<Object> createScenario(Scenario scenario, UserInfo loginUser,Integer projectId);//创建项目
    CommonResponse<Object> checkFiled(String fieldName, String fieldValue, Integer projectId);//参数校验
    CommonResponse<Project> getScenarioDetail(Integer ScenarioId);//获取项目详细信息
    CommonResponse<Page<Project>> getScenarioList(Integer userId, Integer projectId,String keyword, String orderBy, int pageNum, int pageSize);//查询项目列表
    CommonResponse<Integer> getScenarioId(Integer userId,String scenarioName);//获取项目id，查询的首要
}
