package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

/**
 * @program: community
 * @description:
 * @author: chen nengzhen
 * @create: 2022-06-20 22:39
 **/

@Repository("alphaHibernate")
public class AlphaDaoHibernateImpl implements AlphaDao{
    @Override
    public String select() {
        return "Hibernate";
    }
}
