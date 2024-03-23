package kiyoshi.webtest.hikari.controller.front;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kiyoshi.webtest.hikari.common.CONSTANT;
import kiyoshi.webtest.hikari.common.CommonResponse;
import kiyoshi.webtest.hikari.domain.Project;
import kiyoshi.webtest.hikari.domain.UserInfo;
import kiyoshi.webtest.hikari.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
@Validated//我们对参数进行校验采用validation时需要在使用的类上写上这个注解，不然是不会起效的
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @PostMapping("create")
    public CommonResponse<Object> create(
            @RequestBody @Valid Project project,
            HttpSession session
    ) {
        UserInfo loginUser = (UserInfo)session.getAttribute(CONSTANT.LOGIN_USER);
        if(loginUser == null){
            return CommonResponse.createForError("user need login");
        }
        else {
            CommonResponse<Object> result = projectService.createProject(project,loginUser);
            return result;
        }
    }

    @GetMapping("id")
    public CommonResponse<Integer> getProjectId(HttpSession session,String projectName){
        UserInfo loginUser = (UserInfo)session.getAttribute(CONSTANT.LOGIN_USER);
        if(loginUser == null){
            return CommonResponse.createForError("user need login");
        }
        else {
            CommonResponse<Integer> projectId = projectService.getProjectId(loginUser.getId(),projectName);
            return projectId;
        }
    }

    @GetMapping("detail")
    public CommonResponse<Object> getProjectDetail(HttpSession session,String projectName){
        UserInfo loginUser = (UserInfo)session.getAttribute(CONSTANT.LOGIN_USER);
        if(loginUser == null){
            return CommonResponse.createForError("user need login");
        }
        else {
            CommonResponse<Integer> projectId = projectService.getProjectId(loginUser.getId(),projectName);
            Integer id = projectId.getData();
            return CommonResponse.createForSuccess(projectService.getProjectDetail(id));

        }
    }

    @GetMapping("list")
    public CommonResponse<Object> getProjectList(
            HttpSession session,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "") String orderBy,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "2") int pageSize
    ){
        UserInfo loginUser = (UserInfo)session.getAttribute(CONSTANT.LOGIN_USER);
        if(loginUser == null){
            return CommonResponse.createForError("user need login");
        }
        else {
            Integer userId = loginUser.getId();
            return CommonResponse.createForSuccess(projectService.getProjectList(userId,keyword,orderBy,pageNum,pageSize));

        }
    }
}
