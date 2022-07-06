package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CookieUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @program: community
 * @description:
 * @author: chen nengzhen
 * @create: 2022-07-05 22:39
 **/
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 从request中获取cookie，最好封装一下，便于其他功能使用
        String ticket = CookieUtil.getValue(request, "ticket");
        System.out.println("ticket: " + ticket);
        if (ticket != null) {
            // 不为空表示用户已登录，那就查询ticket，找到用户信息
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            // 判断凭证是否有效，如果超时就无效，有效才暂存
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                // 凭证有效，利用凭证查询用户
                User user = userService.findUserById(loginTicket.getUserId());
                // 查到用户以后，是在后续处理过程中使用，为了后续使用，需要暂存用户信息(在本次请求中持有用户)
                hostHolder.setUsers(user);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
