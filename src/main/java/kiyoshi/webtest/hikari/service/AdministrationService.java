package kiyoshi.webtest.hikari.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import kiyoshi.webtest.hikari.common.CommonResponse;
import kiyoshi.webtest.hikari.domain.Administration;
import kiyoshi.webtest.hikari.domain.UserInfo;

public interface AdministrationService {
    CommonResponse<Object> createAdministration(Administration administration, Integer scenarioId);//创建项目
    CommonResponse<Object> checkFiled(String fieldName, String fieldValue, Integer scenarioId);//参数校验
    CommonResponse<Administration> getAdministrationDetail(Integer AdministrationId);//获取调度详细信息
    CommonResponse<Page<Administration>> getAdministrationList(Integer scenarioId, String keyword, String orderBy, int pageNum, int pageSize);//查询项目列表
    CommonResponse<Integer> getAdministrationId(Integer scenarioId,String administrationName);//获取id，查询的首要

    CommonResponse<Object> administrate(Integer administrationId, UserInfo loginUser);
}
