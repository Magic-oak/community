package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author joe
 */
@Mapper
public interface DiscussPostMapper {
    /** 这个方法有一个参数userId，数据库的帖子表里也有usrid这个字段。这个方法考虑到通用性，针对社区首页，要显示所有用户的帖子，那就不需要userid，
     * 因为所有用户的帖子都要查；当开发用户界面时，要显示当前用户已发布的帖子，那么就需要传入当前用户id，查询当前用户的帖子。所以这是一个动态sql，
     * 当传入参数为0,就不拼接userId了，表示查询所有用户的帖子，当传入其他数字时，就是查询指定用户帖子。
     * @param userId 0：不指定用户；非0：返回指定用户comment。
     * @param offset 起始页
     * @param limit 条数
     * @param orderMode 排序类型（升序、降序等）
     * @return comments
     */
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit, int orderMode);

    /**
     * 这里注解@Param的作用是给参数取一个别名，比如说当方法里的参数名字比较长时，写到sql中比较麻烦，那就可以给他取个别名。
     * 如果只有一个参数，并且在<if>里使用，则必须使用别名.
     * 如果指定userId，则返回指定用户id的comments数量，否则返回全部comment数量。
     * @param userId 0：不指定用户；非0：返回指定用户comment。
     * @return comment数量（不包含被拉黑的帖子）
     */
    int selectDiscussPostRows(@Param("userId") int userId);

    /**
     * 向表中插入一条帖子
     * @param discussPost 待插入帖子
     * @return 插入是否成功（1表示成功，0表示失败）
     */
    int insertDiscussPost(DiscussPost discussPost);

    /**
     * 根据帖子id查询帖子
     * @param id 帖子id
     * @return 返回查询到的帖子
     */
    DiscussPost selectDiscussPostById(int id);

    /**
     * 更新某个帖子的评论数
     * @param id 帖子id
     * @param commentCount 新的评论数
     * @return 数据更新成功则返回1，否则返回0
     */
    int updateCommentCount(int id, int commentCount);

    /**
     * 根据帖子id更新帖子类型
     * @param id 帖子id
     * @param type 帖子类型
     * @return 更新是否成功
     */
    int updateType(int id, int type);

    /**
     * 更新帖子状态
     * @param id 帖子id
     * @param status 帖子状态
     * @return 更新是否成功
     */
    int updateStatus(int id, int status);

    /**
     * 更新帖子综合分数（这个分数是用来给帖子排序的，高分帖子将展示在首页前排）
     * @param id 帖子id
     * @param score 帖子分数
     * @return 更新是否成功。
     */
    int updateScore(int id, double score);
}
