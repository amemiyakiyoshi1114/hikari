package kiyoshi.webtest.hikari.persistence;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import kiyoshi.webtest.hikari.domain.Project;

public interface ProjectMapper extends BaseMapper<Project> {
    long selectCount(QueryWrapper<String> username);

}
