package com.nowcoder.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author joe
 * 利用Sprint Initializr（创建Spring Boot项目的引导工具）创建一个Spring Boot项目
 * spring boot以jar包的方式内嵌了一个Tomcat服务器，所以在运行这个java文件时，下面的命令行窗口中会显示已启用Tomcat服务器，端口为8080.
 */
@SpringBootApplication
public class CommunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommunityApplication.class, args);
	}

}
