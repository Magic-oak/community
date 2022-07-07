package com.nowcoder.community.util;

import com.nowcoder.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * @program: community
 * @description: 这个工具起到容器的作用，用于代替session对象，保存用户信息，实现线程隔离。
 * @author: chen nengzhen
 * @create: 2022-07-05 22:53
 **/
@Component
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUsers(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}
