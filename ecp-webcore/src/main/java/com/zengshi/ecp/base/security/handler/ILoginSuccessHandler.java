/**
 * ILoginSuccessHandler.java	  V1.0   2013-4-23 上午8:58:05
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
 * 功能描述：登录成功后，处理类
 *
 *
 * 修改历史：(修改人，修改时间，修改原因/内容)
 */
public interface ILoginSuccessHandler {
    /**
     * 
     * 功能描述：处理方法
     *
     * 创建日期 ：2013-4-23 上午8:59:48
     *
     * @param request
     *
     * 修改历史 ：(修改人，修改时间，修改原因/内容)
     */
    void successHandle(HttpServletRequest request);
}
