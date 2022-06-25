package com.nowcoder.community.controller;

import com.nowcoder.community.service.AlphaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @program: community
 * @description: controller处理浏览器请求，在处理浏览器请求的过程中，会调用业务组件（service）处理当前业务，业务组件会调用dao去访问数据库。
 * controller调用service，service调用dao。它们是彼此互相依赖的，它们的依赖关系可以通过依赖注入去实现。
 * @author: chen nengzhen
 * @create: 2022-06-20 21:46
 **/
@Controller
@RequestMapping("/alpha")

public class AlphaController {
    @Autowired
    private AlphaService alphaService;

    /**
     * 写一个能处理浏览器请求的方法
     * 下面第一个注解@RequestMapping作用是声明响应路径
     */
    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello() {
        return "hello Sprint boot";
    }

    @RequestMapping("/data")
    @ResponseBody
    public String getData() {
        return alphaService.find();
    }

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取请求数据
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + ": " + value);
        }
        System.out.println(request.getParameter("code"));

        // response像浏览器返回响应数据
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write("<h1>nowcoder</h1>");
        writer.close();
    }

    // Get请求
    // /student?current=1&limit=20
    @RequestMapping(path = "/student", method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(
            @RequestParam(name = "current", required = false, defaultValue = "1") int current,
            @RequestParam(name = "limit", required = false, defaultValue = "20") int limit) {
        System.out.println(current);
        System.out.println(limit);
        return "some students";
    }

    // /student/123
    @RequestMapping(path = "/student/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id) {
        System.out.println(id);
        return "a student";
    }

    // POST请求
    @RequestMapping(path = "/student", method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name, int age) {
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    // 向浏览器返回响应数据
    // 向浏览器响应一个动态的HTML数据。这里因为是返回HTML，所以就不要加@ResponseBody注解了。不加这个注解，默认返回的就是HTML了。返回类型是
    // ModelAndView类型，即Mode和View两类数据
    @RequestMapping(path = "/teacher", method = RequestMethod.GET)
    public ModelAndView getTeacher() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name", "张三");
        modelAndView.addObject("age", 30);
        modelAndView.setViewName("/demo/view");
        return modelAndView;
    }

    @RequestMapping(path = "/school", method = RequestMethod.GET)
    public String getSchool(Model model) {
        model.addAttribute("name", "西北工业大学");
        model.addAttribute("age", 100);
        return "/demo/view";
    }

    // 向浏览器响应JSON数据（异步请求）
    // java对象 -> JSON字符串 -> JS对象、C#对象...
    // DispatcherServlet调用这个方法时，看到加了@ResponseBody这个注解，方法声明返回的类型是Map<>，它自动会将这个map转为字符串，发送给浏览器。
    @RequestMapping(path = "/employee", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getEmployee() {
        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "张三丰");
        emp.put("age", 25);
        emp.put("salary", 20000);
        return emp;
    }

    @RequestMapping(path = "/employees", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getEmployees() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "张三丰");
        emp.put("age", 25);
        emp.put("salary", 20000);
        list.add(emp);
        Map<String, Object> emp2 = new HashMap<>();
        emp2.put("name", "李四光");
        emp2.put("age", 55);
        emp2.put("salary", 25000);
        list.add(emp2);
        Map<String, Object> emp3 = new HashMap<>();
        emp3.put("name", "于子清");
        emp3.put("age", 22);
        list.add(emp3);
        return list;
    }
}
