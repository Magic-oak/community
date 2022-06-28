package com.nowcoder.community.util;

/**
 * @author joe
 */
public interface CommunityConstant {
    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS = 0;
    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT = 1;

    /**
     * 激活失败
     */
    int ACTIVATION_FAILURE = 2;

    /**
     * 默认状态登录凭证超时时间(12h)
     */
    int DEFAULT_EXPIRED_SECOND = 3600 * 12;

    /**
     * 记住状态下的登录超时凭证
     */
    int REMEMBER_EXPIRED_SECOND = 3600 * 24 * 30;
}
