package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @program: community
 * @description: 这是个业务beam,希望这个beam能够被容器管理，所以加上注解，业务组件加的注解是@Service
 * @author: chen nengzhen
 * @create: 2022-06-20 22:50
 **/
@Service
@Scope("singleton")
public class AlphaService {
    /**
     *  把AlphaDao注入给AlphaService
     *  这里，@AutoWried注解能够用在构造器或者setter方法或者成员变量上，作用就是把AlphaService类依赖的Bean注入进来。
     *  */
    @Autowired
    private AlphaDao alphaDao;

    public AlphaService() {
        System.out.println("实例化AlphaService");
    }

    /**
     * 这个注解@PostConstruct的意思是这个方法会在构造器之后调用
     */
    @PostConstruct
    public void init() {
        System.out.println("初始化AlphaService");
    }

    /**
     * 这个注解的意思是在对象销毁之前调用它
     */
    @PreDestroy
    public void destroy() {
        System.out.println("销毁AlphaService");
    }

    // 模拟查询业务
    public String find() {
        return alphaDao.select();
    }
}
