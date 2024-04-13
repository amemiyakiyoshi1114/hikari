package kiyoshi.webtest.hikari;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import kiyoshi.webtest.hikari.domain.JenkinsReportJson;
import kiyoshi.webtest.hikari.domain.UserInfo;
import kiyoshi.webtest.hikari.persistence.UserInfoMapper;
import kiyoshi.webtest.hikari.service.impl.ReportServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class HikariApplicationTests {
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Test
	public void test() {
		//如果自动注入成功，那么说明成功的构造了接口相应的类
//		System.out.println(userInfoMapper);
//		QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
//		wrapper.equals("username");
//		List<UserInfo> userList = userInfoMapper.selectList(wrapper);
//		System.out.println(userList);

		String jsonData = "{\"_class\":\"org.jenkinsci.plugins.workflow.job.WorkflowRun\",\"actions\":[{\"_class\":\"hudson.model.CauseAction\",\"causes\":[{\"_class\":\"org.jenkinsci.plugins.gwt.GenericCause\",\"shortDescription\":\"tsubaki\"}]},{\"_class\":\"hudson.model.ParametersAction\",\"parameters\":[]},{\"_class\":\"jenkins.metrics.impl.TimeInQueueAction\",\"blockedDurationMillis\":0,\"blockedTimeMillis\":0,\"buildableDurationMillis\":0,\"buildableTimeMillis\":5,\"buildingDurationMillis\":18056,\"executingTimeMillis\":17654,\"executorUtilization\":0.98,\"subTaskCount\":1,\"waitingDurationMillis\":8423,\"waitingTimeMillis\":8423},{\"_class\":\"org.jenkinsci.plugins.workflow.libs.LibrariesAction\"},{},{},{\"_class\":\"hudson.plugins.git.util.BuildData\",\"buildsByBranchName\":{\"refs/remotes/origin/main\":{\"_class\":\"hudson.plugins.git.util.Build\",\"buildNumber\":21,\"buildResult\":null,\"marked\":{\"SHA1\":\"930268bc2db60292d1ec40d87214f48dc270ff43\",\"branch\":[{\"SHA1\":\"930268bc2db60292d1ec40d87214f48dc270ff43\",\"name\":\"refs/remotes/origin/main\"}]},\"revision\":{\"SHA1\":\"930268bc2db60292d1ec40d87214f48dc270ff43\",\"branch\":[{\"SHA1\":\"930268bc2db60292d1ec40d87214f48dc270ff43\",\"name\":\"refs/remotes/origin/main\"}]}}},\"lastBuiltRevision\":{\"SHA1\":\"930268bc2db60292d1ec40d87214f48dc270ff43\",\"branch\":[{\"SHA1\":\"930268bc2db60292d1ec40d87214f48dc270ff43\",\"name\":\"refs/remotes/origin/main\"}]},\"remoteUrls\":[\"$repositoryURL\"],\"scmName\":\"\"},{},{\"_class\":\"org.jenkinsci.plugins.workflow.cps.EnvActionImpl\"},{},{\"_class\":\"hudson.tasks.junit.TestResultAction\",\"failCount\":0,\"skipCount\":0,\"totalCount\":2,\"urlName\":\"testReport\"},{},{},{},{\"_class\":\"org.jenkinsci.plugins.displayurlapi.actions.RunDisplayAction\"},{\"_class\":\"org.jenkinsci.plugins.pipeline.modeldefinition.actions.RestartDeclarativePipelineAction\"},{},{\"_class\":\"org.jenkinsci.plugins.workflow.job.views.FlowGraphAction\"},{},{},{}],\"artifacts\":[{\"displayPath\":\"allure-report.zip\",\"fileName\":\"allure-report.zip\",\"relativePath\":\"allure-report.zip\"},{\"displayPath\":\"TestNGAndSelenium-1.0-SNAPSHOT.jar\",\"fileName\":\"TestNGAndSelenium-1.0-SNAPSHOT.jar\",\"relativePath\":\"target/TestNGAndSelenium-1.0-SNAPSHOT.jar\"}],\"building\":false,\"description\":null,\"displayName\":\"#21\",\"duration\":18056,\"estimatedDuration\":25076,\"executor\":null,\"fullDisplayName\":\"webhookTry #21\",\"id\":\"21\",\"keepLog\":false,\"number\":21,\"queueId\":109,\"result\":\"SUCCESS\",\"timestamp\":1711338961601,\"url\":\"http://100.64.240.27:8080/job/webhookTry/21/\",\"changeSets\":[],\"culprits\":[],\"inProgress\":false,\"nextBuild\":{\"number\":22,\"url\":\"http://100.64.240.27:8080/job/webhookTry/22/\"},\"previousBuild\":{\"number\":20,\"url\":\"http://100.64.240.27:8080/job/webhookTry/20/\"}}";

		Gson gson = new Gson();
		JenkinsReportJson jenkinsJob = gson.fromJson(jsonData, JenkinsReportJson.class);
		System.out.println(jenkinsJob.getUrl());
	}

}
