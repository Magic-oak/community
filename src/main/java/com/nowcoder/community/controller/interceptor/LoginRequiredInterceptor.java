package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.Mergeable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @program: community
 * @description:
 * @author: chen nengzhen
 * @create: 2022-07-07 15:24
 **/
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {
    @Autowired
    private HostHolder hostHolder;

    /**
     * 如何判断是否登录？尝试从HostHolder中取用户，如果能取到，说明登录了，否则未登录。
     * 处理逻辑：
     * 首先，参数中Object是我们拦截的目标，判断我们拦截的目标是不是一个方法，如果不是方法就不拦截（如静态资源），是方法就接着处理。
     * 如何判断是否是方法？如果拦截到的是一个方法， 那就handler将是HandlerMethod类型
     * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如何判断是否是方法？如果拦截到的是一个方法， 那就handler将是HandlerMethod类型
        if (handler instanceof HandlerMethod) {
            // handler是HandlerMethod类型，也就是说，我们拦截到的目标是方法，那就把它转为HandlerMethod类型， 方便后续获取它的内容。
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 通过调用getMethod()方法直接获取拦截到的方法的对象。
            Method method = handlerMethod.getMethod();
            // 有了方法对象，就可以从方法对象上取注解，这里按照指定类型取注解。
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            // 如果注解loginRequired不为空，说明这个方法是需要登录才能访问的，那就需要验证用户是否登录，如果用户没有登录，
            // 即获取不到user，那就重定向到登录页面
            if (loginRequired != null && hostHolder.getUser() == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }
        }
        return true;
    }
}
