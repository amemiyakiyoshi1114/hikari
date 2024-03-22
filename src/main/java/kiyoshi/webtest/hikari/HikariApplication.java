package kiyoshi.webtest.hikari;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("kiyoshi.webtest.hikari.persistence")
public class HikariApplication {

	public static void main(String[] args) {
		SpringApplication.run(HikariApplication.class, args);
	}

}
