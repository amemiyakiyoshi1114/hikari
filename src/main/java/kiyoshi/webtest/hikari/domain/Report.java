package kiyoshi.webtest.hikari.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Report {
    private String username;
    private String projectName;
    private String scenarioName;
    private String administrationName;
    private Integer id;
    private LocalDateTime createTime;
}
