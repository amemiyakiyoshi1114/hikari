package kiyoshi.webtest.hikari.domain;

import com.google.gson.JsonObject;
import lombok.Data;

import java.util.List;

@Data
public class JenkinsReportJson {
    private String _class;
    private List actions;
    private List artifacts;
    private Boolean building;
    private String description;
    private String displayName;
    private Integer duration;
    private Integer estimatedDuration;
    private String executor;
    private String fullDisplayName;
    private String id;
    private Boolean keepLog;
    private Integer number;
    private Integer queueId;
    private String result;
    private long timestamp;
    private String url;
    private List changeSets;
    private List culprits;
    private Boolean inProgress;
    private JsonObject nextBuild;
    private JsonObject previousBuild;
}
