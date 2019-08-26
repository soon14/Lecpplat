/**
 * SrpMappingExceptionResolver.java	  V1.0   2013-3-29 下午4:51:31
 *
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.zengshi.ecp.base.mvc.resolver;

import com.zengshi.ecp.base.mvc.view.EcpExceptionMappingJackson2JsonView;
import com.zengshi.ecp.base.util.WebContextUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class EcpMappingExceptionResolver extends SimpleMappingExceptionResolver {
    
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request,HttpServletResponse response, Object handler, Exception ex) {
        boolean isAjax= WebContextUtil.isAjaxRequest(request);
        if(!isAjax){
            return super.doResolveException(request, response, handler, ex);
        }else{
            return new ModelAndView(new EcpExceptionMappingJackson2JsonView(ex));
        }
    }

}
