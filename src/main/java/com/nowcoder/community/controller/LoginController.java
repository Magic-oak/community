package com.nowcoder.community.controller;

import com.google.code.kaptcha.Producer;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClient;
import org.apache.catalina.mbeans.SparseUserDatabaseMBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.interceptor.AbstractCacheResolver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.SpringServletContainerInitializer;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @program: community
 * @description:
 * @author: chen nengzhen
 * @create: 2022-06-26 10:19
 **/

//  通常会加上RequestMapping注解，这里不加了，不加访问的就是方法。
@Controller
public class LoginController implements CommunityConstant {
    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    private static final Logger logger = LoggerFactory.getLogger("LoginController");

    // 为了简化读取properties文件中的配置值，spring支持@Value注解的方式来获取，这种方式大大简化了项目配置，提高业务中的灵活性。
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private MailClient mailClient;

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage() {
        return "/site/register";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLoginPage() {
        return "/site/login";
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(Model model, User user) {
        Map<String, Object> map = userService.register(user);
        if (map == null || map.isEmpty()) {
            // 注册成功，给出提示，并跳转到首页
            model.addAttribute("msg", "注册成功，我们已经向您的邮箱发送了一封激活邮件，请尽快激活。");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/site/register";
        }
    }

    // http://localhost:8080/community/activation/101/code
    @RequestMapping(path = "/activation/{userId}/{code}", method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code) {
        int result = userService.activation(userId, code);
        if (result == ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "激活成功，您的账号已经可以正常使用。");
            model.addAttribute("target", "/login");
        } else if (result == ACTIVATION_REPEAT) {
            model.addAttribute("msg", "无效操作，该账号已经激活");
            model.addAttribute("target", "/index");
        } else {
            model.addAttribute("msg", "激活失败，您提供的激活码不正确");
            model.addAttribute("target", "/index");
        }
        return "/site/operate-result";
    }

    //生成验证码的方法
    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        System.out.println("点击刷新");
        // 生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        //将验证码存入session
        session.setAttribute("kaptcha", text);

        //将图片输出给浏览器
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            logger.error("响应验证码失败: " + e.getLocalizedMessage());
        }
    }

    /**
     * 上面已经有一个login方法了，这里又写一个login方法，是否会冲突呢？如果method也相同，path也相同，那就回冲突，但是如果method不同，
     * 比如上面是Get方法，这里是Post方法，那么就不会冲突。
     *
     * 关于验证码需要提一句：用户打开登录界面，服务端会生成一个验证码，把这个验证码存放到服务端的session中，然后再把验证码发送到客户端。客户端用户
     * 输入登录信息（包括用户输入的验证码），点击提交发送到服务端，服务端取出验证码code，与服务端session中保存的验证码kaptcha进行比较，如果相同
     * 则进行下一步验证。
     *
     * 方法参数是需要页面表单传进来的一些参数，
     * @param username   post表单提交到服务端的用户名
     * @param password   post表单提交到服务端的密码
     * @param code       验证码
     * @param rememberMe 记住我
     * @param model      返回数据时需要用到Model
     * @param session    页面传到服务端的验证码，需要和用户打开登录页面时生成的验证码比较，这个生成的验证码在服务端是保存在session中的。
     * @param response   如果登录成功了，我们需要把ticket发送到客户端让它保存在cookie中，要创建cookie还需要HttpServletResponse.
     * @return
     */
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String username, String password, String code, boolean rememberMe, Model model, HttpSession session, HttpServletResponse response) {
        // 判断验证码对不对（这个在前端做）
        String kaptcha = (String) session.getAttribute("kaptcha");
        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)) {
            model.addAttribute("codeMsg", "验证码不正确");
            return "/site/login";
        }
        // 检查账号、密码（这些交给业务层去做）
        int expired_seconds = rememberMe ? CommunityConstant.REMEMBER_EXPIRED_SECOND : CommunityConstant.DEFAULT_EXPIRED_SECOND;
        Map<String, Object> map = userService.login(username, password, expired_seconds);
        if (map.containsKey("ticket")) {
            // 给客户端发送一个cookie
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            // 我们通常需要给cookie设置有效路径，用户登录以后，它的凭证有效范围应该是包含在整个项目内，访问项目的任何页面登录状态都应该是有效的。
            cookie.setPath(contextPath);
            // 设置cookie的过期时间
            cookie.setMaxAge(expired_seconds);
            //把cookie发送给页面，在响应时就会发送给浏览器
            response.addCookie(cookie);

            // 登录成功后为什么是重定向到首页而不是return "/index"?
            // 原因：你当前的请求是登录，却给浏览器返回了首页的模板，很让人困惑的，重定向是重新发起了一个请求，浏览器的地址发生了改变，逻辑是严谨的。
            return "redirect:/index";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/login";
        }

    }

    // 退出不需要通过表单向浏览器提交什么数据，所以用GET请求就可以。
    // 通过@CookieValue注解要求SpringMVC把它注入进来
    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        // 默认重定向到GET请求的login方法
        return "redirect:/login";
    }

    // 忘记密码页面
    @RequestMapping(path = "/forget", method = RequestMethod.GET)
    public String forget() {
        return "/site/forget";
    }

    // 获取验证码
    @RequestMapping(path = "/forget/code", method = RequestMethod.GET)
    @ResponseBody
    public String getForgetCode(String email, HttpSession session) {
        if (StringUtils.isBlank(email)) {
            return CommunityUtil.getJSONString(1, "邮箱不能为空！");
        }

        // 发送邮件
        Context context = new Context();
        context.setVariable("email", email);
        String code = CommunityUtil.generateUUID().substring(0, 4);
        context.setVariable("verifyCode", code);
        String content = templateEngine.process("/mail/forget", context);
        mailClient.sendMain(email, "找回密码", content);

        //保存验证码
        session.setAttribute("verifyCode", code);
        return CommunityUtil.getJSONString(0);
    }

    // 上传用户头像的操作在表现层实现，因为处理（头像）文件要用的是MultipartFile对象，而这个对象是属于SpringMVC的对象，是表现层的对象，如果
    // 把它传给Service层，那么Service层就和表现层有了耦合，这样不好， 所以就在表现层做上传头像的操作了。业务层只处理更新路径的操作。

    // 重置密码
    @RequestMapping(path = "/forget/password", method = RequestMethod.POST)
    public String resetPassword(String email, String verifyCode, String password, Model model, HttpSession session) {
        String code = (String) session.getAttribute("verifyCode");
        if (StringUtils.isBlank(code) || StringUtils.isBlank(verifyCode) || !code.equalsIgnoreCase(verifyCode)) {
            System.out.println("verifyCode=" + verifyCode + "   code=" + code);
            model.addAttribute("codeMsg", "验证码错误");
            return "/site/forget";
        }

        Map<String, Object> map = userService.updatePassword(email, password);
        if (map.containsKey("user")) {
            return "redirect:/login";
        }else {
            model.addAttribute("emailMsg", map.get("emailMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/forget";
        }
    }
}
