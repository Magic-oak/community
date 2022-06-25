package com.nowcoder.community.entity;

/**
 * @program: community
 * @description: 封装分页相关信息
 * @author: chen nengzhen
 * @create: 2022-06-25 15:18
 **/

public class Page {
    /**
     * 当前页码
     */
    private int current = 1;
    /**
     * 显示上限
     */
    private int limit = 10;
    /**
     * 数据总数(一共有多少行数据，用来计算总页数，比如100条数据，每页最多显示10条，那么就一共有10页)
     */
    private int rows;
    /**
     * 查询路径（用于分页链接）
     */
    private String path;

    public int getCurrent() {
        return current;
    }

    public int getLimit() {
        return limit;
    }

    public int getRows() {
        return rows;
    }

    public String getPath() {
        return path;
    }

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取当前页的起始行，计算下一页的起始行
     * @return 总的页数
     */
    public int getOffSet() {
        return (current - 1) * limit;
    }

    /**
     * 获取总的页数
     * @return 总的页数
     */
    public int getTotal() {
        if (rows % limit == 0) {
            return rows / limit;
        }else {
            return rows / limit + 1;
        }
    }

    /**
     * 获取起始页码
     * @return
     */
    public int getFrom() {
        int from = current - 2;
        return Math.max(from, 1);
    }

    /**
     * 获取结束页码
     * @return
     */
    public int getTo() {
        int to = current + 2;
        int total = getTotal();
        return Math.min(to, total);
    }
}
