package kiyoshi.webtest.hikari.controller.front;

import jakarta.servlet.http.HttpSession;
import kiyoshi.webtest.hikari.common.CONSTANT;
import kiyoshi.webtest.hikari.common.CommonResponse;
import kiyoshi.webtest.hikari.domain.UserInfo;
import kiyoshi.webtest.hikari.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    ReportService reportService;

    @GetMapping("id")
    public CommonResponse<Integer> getReportId(HttpSession session, String reportName,Integer administrationId){
        UserInfo loginUser = (UserInfo)session.getAttribute(CONSTANT.LOGIN_USER);
        if(loginUser == null){
            return CommonResponse.createForError("user need login");
        }
        else {
            CommonResponse<Integer> reportId = reportService.getReportId(administrationId,reportName);
            return reportId;
        }
    }

    @GetMapping("detail")
    public CommonResponse<Object> getReportDetail(HttpSession session,String reportName,Integer administrationId){
        UserInfo loginUser = (UserInfo)session.getAttribute(CONSTANT.LOGIN_USER);
        if(loginUser == null){
            return CommonResponse.createForError("user need login");
        }
        else {
            CommonResponse<Integer> reportId = reportService.getReportId(administrationId,reportName);
            return CommonResponse.createForSuccess(reportService.getReportDetail(reportId.getData(),loginUser));

        }
    }

    @GetMapping("list")
    public CommonResponse<Object> getReportList(
            HttpSession session,
            @RequestParam(required = true) Integer administrationId,
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
            return CommonResponse.createForSuccess(reportService.getReportList(administrationId,keyword,orderBy,pageNum,pageSize));

        }
    }
}
