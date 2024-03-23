package kiyoshi.webtest.hikari.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import kiyoshi.webtest.hikari.common.CommonResponse;
import kiyoshi.webtest.hikari.domain.Project;
import kiyoshi.webtest.hikari.domain.UserInfo;

public interface ProjectService {
    CommonResponse<Object> createProject(Project project, UserInfo loginUser);//创建项目
    CommonResponse<Object> checkFiled(String fieldName, String fieldValue, String loginUser);//参数校验
    CommonResponse<Project> getProjectDetail(Integer projectId);//获取项目详细信息
    CommonResponse<Page<Project>> getProjectList(Integer userId, String keyword, String orderBy, int pageNum, int pageSize);//查询项目列表
    CommonResponse<Integer> getProjectId(Integer userId,String projectName);//获取项目id，查询的首要

}
