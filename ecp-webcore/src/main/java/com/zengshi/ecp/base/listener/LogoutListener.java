/** 
 * Date:2015年11月10日下午2:15:27 
 * 
*/
package com.zengshi.ecp.base.listener;

import com.zengshi.ecp.base.util.ApplicationContextUtil;
import com.zengshi.ecp.base.util.WebContextUtil;
import com.zengshi.ecp.server.front.security.AuthPrivilegeResDTO;
import com.zengshi.ecp.server.front.security.ILoginRSV;
import com.zengshi.ecp.server.front.security.LoginLogInfoReqDTO;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.sql.Timestamp;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** 
 * Description: <br>
 * Date:2015年11月10日下午2:15:27  <br>
 * 
 * @since JDK 1.6 
 * @see       
 */
public class LogoutListener implements HttpSessionListener {
    
    private ExecutorService executorService;
    
    public LogoutListener(){
        
        executorService=Executors.newCachedThreadPool();
    }

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        
    }
    

    
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        final AuthPrivilegeResDTO auth= WebContextUtil.getCurrentUser();
        if(null!=auth){
            final String sessionId=(String) event.getSession().getAttribute("sessionid");
            executorService.execute(new Runnable() {
                
                @Override
                public void run() {
                    
                    final ILoginRSV loginRSV= ApplicationContextUtil.getBean("loginRSV",ILoginRSV.class);
                    
                    final LoginLogInfoReqDTO loginLogInfoReqDTO=new LoginLogInfoReqDTO();
                    loginLogInfoReqDTO.setOutTime(new Timestamp(System.currentTimeMillis()));
                    loginLogInfoReqDTO.setSessionId(sessionId);
                    loginLogInfoReqDTO.setStaffId(auth.getStaffId());
                    loginLogInfoReqDTO.setStaffLoginName(auth.getStaffCode());
                    loginRSV.updateOutLoginLog(loginLogInfoReqDTO);
                }
            });
            
        }
    }

}

