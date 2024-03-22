package kiyoshi.webtest.hikari.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Scenario {
    private String username;
    private String projectName;
    private String scenarioName;
    private String githubRepositoryURL;
    private String branch;
    private String testClass;

    private Integer id;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
