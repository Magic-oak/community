package com.nowcoder.community.service;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: community
 * @description:
 * @author: chen nengzhen
 * @create: 2022-06-25 10:01
 **/
@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    /**
     * 声明业务方法，查询某一页的数据
     * @param userId 用户id
     * @param offset 起始行
     * @param limit 每一页最多显示的数据数量
     * @return 返回post列表
     */
    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit) {

        return discussPostMapper.selectDiscussPosts(userId, offset, limit, 0);
    }

    public int findDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }
}
