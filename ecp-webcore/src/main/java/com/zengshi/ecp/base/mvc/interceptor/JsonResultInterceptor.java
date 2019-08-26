/** 
 * Date:2015年8月31日下午7:58:46 
 * 
*/
package com.zengshi.ecp.base.mvc.interceptor;

import com.zengshi.ecp.base.mvc.JsonResultThreadLocal;
import com.zengshi.ecp.base.mvc.annotation.NativeJson;
import com.zengshi.ecp.base.util.WebContextUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/** 
 * Description: <br>
 * Date:2015年8月31日下午7:58:46  <br>
 * 
 * @since JDK 1.6 
 * @see       
 */
public class JsonResultInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        
        if(!WebContextUtil.isAjaxRequest(request)){
            return true;
        }
        
        if(null!=handler && HandlerMethod.class.isAssignableFrom(handler.getClass())){
            HandlerMethod hmethod=(HandlerMethod)handler;
            Method method=hmethod.getMethod();
            NativeJson nj=method.getAnnotation(NativeJson.class);
            if(null!=nj){
                JsonResultThreadLocal.set(!nj.value());
            }
        }
        
        
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) throws Exception {
        
        JsonResultThreadLocal.set(true);
    }

}

