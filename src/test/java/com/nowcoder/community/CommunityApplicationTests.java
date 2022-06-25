package com.nowcoder.community;

import com.nowcoder.community.dao.AlphaDao;
import com.nowcoder.community.service.AlphaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Spring容器是自动创建的。我们通过实现ApplicationContextAware接口，实现setApplicationContext方法，获取到ApplicationContext对象，
 * 全程没有new过容器对象。
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class CommunityApplicationTests implements ApplicationContextAware {
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Test
	public void testApplicationContext() {
		System.out.println(applicationContext);
		// 怎么用这个容器来管理beam
		AlphaDao alphaDao = applicationContext.getBean(AlphaDao.class);
		System.out.println(alphaDao.select());

		alphaDao = applicationContext.getBean("alphaHibernate", AlphaDao.class);
		System.out.println(alphaDao.select());
	}

	@Test
	public void testBeamManagement() {
		AlphaService alphaService = applicationContext.getBean(AlphaService.class);
		//获取到这个beam之后打印出来
		System.out.println(alphaService);
	}

	@Test
	public void testBeanManagement() {
		// 按照类进行获取
		AlphaService alphaService = applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService);
		// 被Spring容器管理的bean默认是单例的。（如果你不希望它是单例的，你希望每次调用getBean()都新建一个实例，也可以，那就需要在Bean的定义上
		// 加一个注解@Scope()，这个注解表示bean的作用范围，是整个容器中有一个还是整个容器中有多个，默认参数@Scope("singleton")表示单例，
		// @Scope("prototype")表示多个实例，每次访问bean,都会创建一个新的实例。项目中基本都是用单例，很少用多例。
		alphaService = applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService);
	}

	@Test
	public void testBeanConfig() {
		SimpleDateFormat simpleDateFormat = applicationContext.getBean(SimpleDateFormat.class);
		// 使用SimpleDateFormat格式化当前日期
		System.out.println(new Date());
		System.out.println(simpleDateFormat.format(new Date()));
	}

	// 下面注解展示的是依赖注入，意思是希望Spring容器能够把AlphaDao注入给这个属性
	//  @Qualifier 用于声明Bean的名称，该注解既可以引用Bean的自定义名称，也可以引用Bean的默认名称。默认名称就是类的原名，自定义名称就是自己
	// 起一个名字。
	@Autowired
	@Qualifier("alphaHibernate")
	private AlphaDao alphaDao;

	@Autowired
	private AlphaService alphaService;

	@Autowired
	private SimpleDateFormat simpleDateFormat;

	//测试依赖注入
	@Test
	public void testDI() {

		System.out.println(alphaDao);
		System.out.println(alphaService);
		System.out.println(simpleDateFormat);
	}
}
