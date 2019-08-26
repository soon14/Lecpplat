/**
 * AuthenticationCheckCodeException.java	  V1.0   2013-2-28 上午11:10:18
 *
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.zengshi.ecp.base.security;

import org.springframework.security.core.AuthenticationException;

/**
 * 
 * 功能描述：
 *
 *
 * 修改历史：(修改人，修改时间，修改原因/内容)
 */
public class AuthenticationCheckCodeException extends AuthenticationException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -8007128049566389267L;

    public AuthenticationCheckCodeException(String msg) {

        super(msg);
    }

    public AuthenticationCheckCodeException(String msg, Throwable t) {
        super(msg, t);
    }
}
