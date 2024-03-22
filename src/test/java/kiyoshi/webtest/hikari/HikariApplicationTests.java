package kiyoshi.webtest.hikari;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import kiyoshi.webtest.hikari.domain.UserInfo;
import kiyoshi.webtest.hikari.persistence.UserInfoMapper;
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
		//����Զ�ע��ɹ�����ô˵���ɹ��Ĺ����˽ӿ���Ӧ����
		System.out.println(userInfoMapper);
		QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
		wrapper.equals("username");
		List<UserInfo> userList = userInfoMapper.selectList(wrapper);
		System.out.println(userList);
	}

}
