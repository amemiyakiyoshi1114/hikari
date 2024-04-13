package kiyoshi.webtest.hikari.controller.front;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kiyoshi.webtest.hikari.common.CONSTANT;
import kiyoshi.webtest.hikari.common.CommonResponse;
import kiyoshi.webtest.hikari.domain.Scenario;
import kiyoshi.webtest.hikari.domain.UserInfo;
import kiyoshi.webtest.hikari.service.ScenarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scenario")
public class ScenarioController {
    @Autowired
    ScenarioService scenarioService;

    @PostMapping("create")
    public CommonResponse<Object> create(
            @RequestBody @Valid Scenario scenario,
            @RequestParam Integer projectId,
            HttpSession session
    ) {
        UserInfo loginUser = (UserInfo)session.getAttribute(CONSTANT.LOGIN_USER);
        if(loginUser == null){
            return CommonResponse.createForError("user need login");
        }
        else {
            CommonResponse<Object> result = scenarioService.createScenario(scenario,projectId);
            return result;
        }
    }

    @GetMapping("id")
    public CommonResponse<Integer> getScenarioId(HttpSession session, String scenarioName,Integer projectId){
        UserInfo loginUser = (UserInfo)session.getAttribute(CONSTANT.LOGIN_USER);
        if(loginUser == null){
            return CommonResponse.createForError("user need login");
        }
        else {
            CommonResponse<Integer> scenarioId = scenarioService.getScenarioId(projectId,scenarioName);
            return scenarioId;
        }
    }

    @GetMapping("detail")
    public CommonResponse<Object> getScenarioDetail(HttpSession session,String scenarioName,Integer projectId){
        UserInfo loginUser = (UserInfo)session.getAttribute(CONSTANT.LOGIN_USER);
        if(loginUser == null){
            return CommonResponse.createForError("user need login");
        }
        else {
            CommonResponse<Integer> scenarioId = scenarioService.getScenarioId(projectId,scenarioName);
            return CommonResponse.createForSuccess(scenarioService.getScenarioDetail(scenarioId.getData()));

        }
    }

    @GetMapping("list")
    public CommonResponse<Object> getScenarioList(
            HttpSession session,
            @RequestParam(required = true) Integer projectId,
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
            return CommonResponse.createForSuccess(scenarioService.getScenarioList(projectId,keyword,orderBy,pageNum,pageSize));

        }
    }
}
