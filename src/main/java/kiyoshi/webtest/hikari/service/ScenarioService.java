package kiyoshi.webtest.hikari.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import kiyoshi.webtest.hikari.common.CommonResponse;
import kiyoshi.webtest.hikari.domain.Scenario;

public interface ScenarioService {
    CommonResponse<Object> createScenario(Scenario scenario,Integer projectId);//创建项目
    CommonResponse<Object> checkFiled(String fieldName, String fieldValue, Integer projectId);//参数校验
    CommonResponse<Scenario> getScenarioDetail(Integer scenarioId);//获取项目详细信息
    CommonResponse<Page<Scenario>> getScenarioList( Integer projectId,String keyword, String orderBy, int pageNum, int pageSize);//查询项目列表
    CommonResponse<Integer> getScenarioId(Integer projectId,String scenarioName);//获取id，查询的首要
}
