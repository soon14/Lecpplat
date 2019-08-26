/** 
 * Date:2015年11月9日下午8:57:55 
 * 
*/
package com.zengshi.ecp.base.security.handler.support;

import com.zengshi.ecp.base.security.handler.ILoginFailureHandler;
import com.zengshi.ecp.base.util.WebContextUtil;
import com.zengshi.ecp.server.front.security.AuthPrivilegeResDTO;
import com.zengshi.ecp.server.front.security.ILoginRSV;
import com.zengshi.ecp.server.front.security.LoginAccessLogReqDTO;
import com.zengshi.ecp.server.front.security.LoginLogInfoReqDTO;
import com.zengshi.ecp.server.front.util.SiteLocaleUtil;
import com.zengshi.paas.utils.InetTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** 
 * Description: <br>
 * Date:2015年11月9日下午8:57:55  <br>
 * 
 * @since JDK 1.6 
 * @see       
 */
public class ECPLoginFailureHander implements ILoginFailureHandler {

//    @Resource
    @Autowired(required = false)
    @Qualifier("loginRSV")
    private ILoginRSV loginRSV;

    public void setLoginRSV(ILoginRSV loginRSV) {

        this.loginRSV = loginRSV;
    }

    private ExecutorService executorService;
    
    public ECPLoginFailureHander(){
        
        executorService=Executors.newCachedThreadPool();
    }
    
    @Override
    public void failureHandle(HttpServletRequest request) {
        
        
        AuthPrivilegeResDTO auth= WebContextUtil.getCurrentUser();
        String staffCode=request.getParameter("j_username");
        Long staffId=-1L;
        if(null!=auth){
            staffId=auth.getStaffId();
        }
        
        final LoginAccessLogReqDTO loginAccessLogReqDTO=new LoginAccessLogReqDTO();
        loginAccessLogReqDTO.setCurrentSiteId(SiteLocaleUtil.getSite());
        loginAccessLogReqDTO.setInIp(InetTool.getClientAddr(request));
        loginAccessLogReqDTO.setLoginFlag("0");
        loginAccessLogReqDTO.setLoginFrom("1");
        loginAccessLogReqDTO.setOperateTime(new Timestamp(System.currentTimeMillis()));
        loginAccessLogReqDTO.setSessionId(request.getSession().getId());
        loginAccessLogReqDTO.setStaffId(staffId);
        loginAccessLogReqDTO.setStaffLoginName(staffCode);
        
        final LoginLogInfoReqDTO loginLogInfoReqDTO=new LoginLogInfoReqDTO();
        loginLogInfoReqDTO.setInIp(request.getRemoteAddr());
        loginLogInfoReqDTO.setCurrentSiteId(SiteLocaleUtil.getSite());
        loginLogInfoReqDTO.setSessionId(request.getSession().getId());
        loginLogInfoReqDTO.setStaffId(staffId);
        loginLogInfoReqDTO.setStaffLoginName(staffCode);
        loginLogInfoReqDTO.setInTime(new Timestamp(System.currentTimeMillis()));
        
        
        executorService.execute(new Runnable() {
            
            @Override
            public void run() {
                loginRSV.insertLoginLog(loginAccessLogReqDTO, loginLogInfoReqDTO);
            }
        });
    }

}

