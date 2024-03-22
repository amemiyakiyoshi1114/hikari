package kiyoshi.webtest.hikari.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

//lombok的@Data注解一键生成get和set方法，我们只写属性就行了
//要注意，分工开发DBA的数据库表名往往不会统一类名，所以需要注解@TableName
@Data
@TableName("userinfo")
public class UserInfo {
    private String username;
    private String password;
    private String JenkinsWorkspace;

    private LocalDateTime createTime;//为什么不用Date类呢？
    private LocalDateTime updateTime;

    private Integer id;
}
