package kiyoshi.webtest.hikari.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("project")
public class Project {
    private String username;
    private String projectName;
    private Integer id;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;


}
