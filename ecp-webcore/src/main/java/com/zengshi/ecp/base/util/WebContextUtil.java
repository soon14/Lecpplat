/**
 * WebContextUtil.java	  V1.0   2013-3-1 下午2:03:18
 *
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.zengshi.ecp.base.util;

import com.zengshi.ecp.base.security.EcpUserDetails;
import com.zengshi.ecp.server.front.security.AuthPrivilegeResDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * 
 * 功能描述：web上下文工具类
 *
 *
 * 修改历史：(修改人，修改时间，修改原因/内容)
 */
public class WebContextUtil {
    protected static final Log logger = LogFactory.getLog(WebContextUtil.class);
    /**
     * 
     * 功能描述：判断request是否异步请求
     *
     * 创建日期 ：2013-3-1 下午3:49:11
     *
     * @param request
     * @return
     *
     * 修改历史 ：(修改人，修改时间，修改原因/内容)
     */
    public static boolean isAjaxRequest(HttpServletRequest request){
//        boolean isAjax = ParamConstant.AJAX_REQUEST_HEAD_VALUE.equals(request.getHeader(ParamConstant.AJAX_REQUEST_HEAD_KEY));
        String ajaxKey=request.getHeader(ParamConstant.AJAX_REQUEST_HEAD_KEY);
        boolean isAjax=StringUtils.hasText(ajaxKey) && ajaxKey.contains(ParamConstant.AJAX_REQUEST_HEAD_VALUE);
        return isAjax;
    }
    
    public static Authentication getAuthentication(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }

    public static boolean isExistSecurityContext(HttpSession session){
        Object contextFromSession= session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        if (contextFromSession == null || !(contextFromSession instanceof SecurityContext)) {

            return false;
        }

        return true;
    }

    public static SecurityContext getSecurityContext(HttpServletRequest request){
        HttpSession session=request.getSession();
        if(null==session){
            return null;
        }
        Object contextFromSession= session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        if (contextFromSession == null || !(contextFromSession instanceof SecurityContext)) {

            return null;
        }

        return (SecurityContext) contextFromSession;
    }
    
    public static EcpUserDetails getUserDetails(){
        Authentication authentication=getAuthentication();
        if(null==authentication || null==authentication.getPrincipal()){
            return null;
        }
        if(null!=authentication.getPrincipal() && EcpUserDetails.class.isAssignableFrom(authentication.getPrincipal().getClass())){
            
            return (EcpUserDetails) authentication.getPrincipal();
        }
        return null;
    }
    /**
     * 
     * getCurrentUser:获取登录用户信息. <br/> 

     * 
     * @return 
     * @since JDK 1.6
     */
    public static AuthPrivilegeResDTO getCurrentUser(){

        EcpUserDetails userDetails=getUserDetails();
        
        
        return userDetails==null?null:userDetails.getAuthPrivilege();
    }
    
    /**
     * 
     * getSessionObject:获取登录存储值. <br/> 
     * 
     * @param key
     * @return 
     * @since JDK 1.6
     */
    public static Object getSessionObject(String key){
        
        return getUserDetails().getAffiliatedObject().get(key);
    }
    /**
     * 
     * logout:用户注销操作. <br/> 
     * 
     * @param request
     * @param response 
     * @since JDK 1.6
     */
    public static void logout(HttpServletRequest request, HttpServletResponse response){
        Assert.notNull(request, "HttpServletRequest required");
        HttpSession session = request.getSession(false);
        if (session != null) {
            logger.debug("Invalidating session: " + session.getId());
            session.invalidate();
        }
        SecurityContext context = SecurityContextHolder.getContext();
        if(null!=context){
            context.setAuthentication(null);
        }
        SecurityContextHolder.clearContext();
    }
}
