package com.nowcoder.community.dao;

import com.nowcoder.community.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 要访问数据库，我们需要在dao层写一个组件。
 * Mybatis开发， 我们管数据访问组件叫xxxMapper, 而且只需要写接口，不用写实现类。
 *
 * 我们需要加一个注解，才能让Spring容器装配这个bean。dao层应该用@Repository。
 * 实际上Mybatis也有一个注解用来标识bean，Mybatis的注解叫@Mapper
 *
 * 要想实现这个Mapper，我们需要提供一个配置文件，配置文件里需要给每个方法提供它所需要的sql。然后Mybatis底层会自动帮我们生成一个实现类。
 * @author joe
 */
@Mapper
public interface UserMapper {

    /**
     * 根据id查询用户
     * @param id 待查询用户ID
     * @return 返回用户实体类对象
     */
    User selectById(int id);

    /**
     * 根据用户名查询用户
     * @param userName 待查询用户名
     * @return 返回用户实体类对象
     */
    User selectByName(String userName);

    /** 根据email查询用户
     * @param email  待查询用户电子邮件
     * @return 返回用户实体类对象
     */
    User selectByEmail(String email);

    /**
     * 向表中插入用户，成功返回插入行数。
     * @param user 待插入表中用户
     * @return 返回插入数据行数，成功就返回1.
     */
    int insertUser(User user);

    /**
     * 修改用户状态
     * @param id 用户ID
     * @param status 用户状态
     * @return 返回修改的条数，修改了几行数据
     */
    int updateStatus(int id, int status);

    /**
     * 更新头像路径
     * @param id 用户ID
     * @param headerUrl 头像路径
     * @return 返回成功与否
     */
    int updateHeader(int id, String headerUrl);

    /**
     * 更新头像路径
     * @param id 用户ID
     * @param newPassword 新密码
     * @return 返回成功与否
     */
    int updatePassword(int id, String newPassword);
}
