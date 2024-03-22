package kiyoshi.webtest.hikari.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Administration {
    private String username;
    private String projectName;
    private String scenarioName;
    private String AdministrationName;

    private Integer id;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
