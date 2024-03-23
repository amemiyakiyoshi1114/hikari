package kiyoshi.webtest.hikari.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import kiyoshi.webtest.hikari.common.CommonResponse;
import kiyoshi.webtest.hikari.domain.Project;
import kiyoshi.webtest.hikari.domain.Scenario;
import kiyoshi.webtest.hikari.domain.UserInfo;
import kiyoshi.webtest.hikari.service.ScenarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("scenarioService")
@Slf4j
public class ScenarioServiceImpl implements ScenarioService {
    @Override
    public CommonResponse<Object> createScenario(Scenario scenario, UserInfo loginUser, Integer projectId) {
        return null;
    }

    @Override
    public CommonResponse<Object> checkFiled(String fieldName, String fieldValue, Integer projectId) {
        return null;
    }

    @Override
    public CommonResponse<Project> getScenarioDetail(Integer ScenarioId) {
        return null;
    }

    @Override
    public CommonResponse<Page<Project>> getScenarioList(Integer userId, Integer projectId, String keyword, String orderBy, int pageNum, int pageSize) {
        return null;
    }

    @Override
    public CommonResponse<Integer> getScenarioId(Integer userId, String scenarioName) {
        return null;
    }
}
