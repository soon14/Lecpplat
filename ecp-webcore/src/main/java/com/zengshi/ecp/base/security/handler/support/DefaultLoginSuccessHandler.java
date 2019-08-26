/**
 * DefaultLoginSuccessHandler.java	  V1.0   2013-11-4 下午4:07:57
 *
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.zengshi.ecp.base.security.handler.support;

import com.zengshi.ecp.base.security.handler.ILoginSuccessHandler;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public class DefaultLoginSuccessHandler implements ILoginSuccessHandler {
    
    private List<ILoginSuccessHandler> handlers;

    @Override
    public void successHandle(HttpServletRequest request) {
        if(!CollectionUtils.isEmpty(handlers)){
            for(ILoginSuccessHandler handler : handlers){
                handler.successHandle(request);
            }
        }
    }

    
    public void setHandlers(List<ILoginSuccessHandler> handlers) {
    
        this.handlers = handlers;
    }

    
}
