package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @program: community
 * @description:
 * @author: chen nengzhen
 * @create: 2022-06-25 22:52
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {

    @Autowired
    private MailClient mailClient;


    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testTexMail() {
        mailClient.sendMain("2425546233@qq.com", "TEST", "Welcome");
    }

    @Test
    public void testHtmlMail() {
        Context context = new Context();
        context.setVariable("username", "陈能真");

        // 模板引擎的作用是帮我们生成动态网页，发邮件还是要靠MailClient.
        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);

        mailClient.sendMain("2425546233@qq.com", "HTML", content);
    }

}
