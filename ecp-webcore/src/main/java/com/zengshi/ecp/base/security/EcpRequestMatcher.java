/**
 * SrpRequestMatcher.java	  V1.0   2013-2-28 下午1:59:25
 *
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.zengshi.ecp.base.security;

import com.zengshi.ecp.base.util.WebContextUtil;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;


/**
 * 
 * 功能描述：request请求缓存规则
 *
 *
 * 修改历史：(修改人，修改时间，修改原因/内容)
 */
public class EcpRequestMatcher implements RequestMatcher {
    /**
     * ajax请求不缓存
     */
    @Override
    public boolean matches(HttpServletRequest request) {
        boolean isAjax = WebContextUtil.isAjaxRequest(request);
        return !isAjax;
    }

}
