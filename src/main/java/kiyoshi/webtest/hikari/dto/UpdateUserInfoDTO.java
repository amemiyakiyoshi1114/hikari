package kiyoshi.webtest.hikari.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserInfoDTO {
    private Integer id;
    @NotBlank(message = "Jenkins workspace name could not be empty")
    private String JenkinsWorkspace;

}
