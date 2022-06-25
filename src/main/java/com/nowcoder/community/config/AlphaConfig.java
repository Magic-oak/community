package com.nowcoder.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 * @program: community
 * @description:
 * @author: chen nengzhen
 * @create: 2022-06-22 09:23
 **/

/**
 * Spring Bean的装配方式2：基于Java代码装配
 * 使用Java代码装配首先需要创建一个配置类
 * 下面@Configuration表明该类是一个配置类，@Bean注解用于声明一个Bean，@Bean注解的方法通知Spring该方法会返回一个Bean并且会注册为Spring应用
 * 上下文的Bean。
 */
@Configuration
public class AlphaConfig {
    /**
     * 这个方法返回的对象将会被装配到容器里。Bean的名字就是方法的名字simpleDateFormat.
     * @Bean 用于装配任何bean,既可以是第三方的Bean，也可以是自定义Bean。
     * @return
     */
    @Bean
    public SimpleDateFormat simpleDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
