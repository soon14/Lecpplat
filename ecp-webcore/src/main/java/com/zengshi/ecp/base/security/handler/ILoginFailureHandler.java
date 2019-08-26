/**
 * LoginFailureHandler.java	  V1.0   2013-4-23 上午9:00:29
 *
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.zengshi.ecp.base.security.handler;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * 功能描述：登录失败处理类
 *
 *
 * 修改历史：(修改人，修改时间，修改原因/内容)
 */
public interface ILoginFailureHandler {
    /**
     * 
     * 功能描述：处理方法
     *
     * 创建日期 ：2013-4-23 上午9:02:08
     *
     * @param request
     *
     * 修改历史 ：(修改人，修改时间，修改原因/内容)
     */
    void failureHandle(HttpServletRequest request);
}
