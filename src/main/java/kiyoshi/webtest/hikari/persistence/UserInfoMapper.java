package kiyoshi.webtest.hikari.persistence;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import kiyoshi.webtest.hikari.domain.UserInfo;

public interface UserInfoMapper extends BaseMapper<UserInfo> {
    long selectCount(QueryWrapper<String> username);
}
