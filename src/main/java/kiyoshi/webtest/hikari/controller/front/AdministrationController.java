package kiyoshi.webtest.hikari.controller.front;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kiyoshi.webtest.hikari.common.CONSTANT;
import kiyoshi.webtest.hikari.common.CommonResponse;
import kiyoshi.webtest.hikari.domain.Administration;
import kiyoshi.webtest.hikari.domain.UserInfo;
import kiyoshi.webtest.hikari.service.AdministrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/administration")
public class AdministrationController {
    @Autowired
    AdministrationService administrationService;

    @PostMapping("create")
    public CommonResponse<Object> create(
            @RequestBody @Valid Administration administration,
            @RequestParam Integer scenarioId,
            HttpSession session
    ) {
        UserInfo loginUser = (UserInfo)session.getAttribute(CONSTANT.LOGIN_USER);
        if(loginUser == null){
            return CommonResponse.createForError("user need login");
        }
        else {
            CommonResponse<Object> result = administrationService.createAdministration(administration,scenarioId);
            return result;
        }
    }

    @GetMapping("id")
    public CommonResponse<Integer> getAdministrationId(HttpSession session, String administrationName,Integer scenarioId){
        UserInfo loginUser = (UserInfo)session.getAttribute(CONSTANT.LOGIN_USER);
        if(loginUser == null){
            return CommonResponse.createForError("user need login");
        }
        else {
            CommonResponse<Integer> administrationId = administrationService.getAdministrationId(scenarioId,administrationName);
            return administrationId;
        }
    }

    @GetMapping("detail")
    public CommonResponse<Object> getAdministrationDetail(HttpSession session,String administrationName,Integer scenarioId){
        UserInfo loginUser = (UserInfo)session.getAttribute(CONSTANT.LOGIN_USER);
        if(loginUser == null){
            return CommonResponse.createForError("user need login");
        }
        else {
            CommonResponse<Integer> administrationId = administrationService.getAdministrationId(scenarioId,administrationName);
            return CommonResponse.createForSuccess(administrationService.getAdministrationDetail(administrationId.getData()));

        }
    }

    @GetMapping("list")
    public CommonResponse<Object> getAdministrationList(
            HttpSession session,
            @RequestParam(required = true) Integer scenarioId,
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
            return CommonResponse.createForSuccess(administrationService.getAdministrationList(scenarioId,keyword,orderBy,pageNum,pageSize));

        }
    }

    @PostMapping("administrate")
    public CommonResponse<Object>administrate(
            HttpSession session,
            @RequestParam(required = true) Integer administrationId){
        UserInfo loginUser = (UserInfo)session.getAttribute(CONSTANT.LOGIN_USER);
        if(loginUser == null){
            return CommonResponse.createForError("user need login");
        }
        else {
            return CommonResponse.createForSuccess(administrationService.administrate(administrationId,loginUser));

        }
    }
}
