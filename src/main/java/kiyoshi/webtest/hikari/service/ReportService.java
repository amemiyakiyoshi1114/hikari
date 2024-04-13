package kiyoshi.webtest.hikari.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import kiyoshi.webtest.hikari.common.CommonResponse;
import kiyoshi.webtest.hikari.domain.Administration;
import kiyoshi.webtest.hikari.domain.Report;
import kiyoshi.webtest.hikari.domain.UserInfo;

public interface ReportService {//唤起调度马上创建报告
    CommonResponse<Page<Report>> getReportList(Integer administrationId, String keyword, String orderBy, int pageNum, int pageSize);//查询报告列表
    CommonResponse<Integer> getReportId(Integer administrationId, String reportName);//获取id，查询的首要
    CommonResponse<Object> getReportDetail(Integer reportId, UserInfo loginUser);//访问数据库中的jenkins_id，拼凑jenkins请求
}
