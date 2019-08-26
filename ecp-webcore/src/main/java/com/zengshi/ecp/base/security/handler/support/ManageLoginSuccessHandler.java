/** 
 * Date:2015年9月21日上午11:40:31 
 * 
*/
package com.zengshi.ecp.base.security.handler.support;

import com.zengshi.ecp.base.security.handler.ILoginSuccessHandler;
import com.zengshi.ecp.base.util.WebContextUtil;
import com.zengshi.ecp.server.front.security.*;
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
 * Date:2015年9月21日上午11:40:31  <br>
 * 
 * @since JDK 1.6 
 * @see       
 */
public class ManageLoginSuccessHandler implements ILoginSuccessHandler {
    
//    @Resource
    @Autowired(required = false)
    @Qualifier("managerRSV")
    private IManagerRSV managerRSV;
    
//    @Resource
    private ILoginRSV loginRSV;
    
    private ExecutorService executorService;

    public void setManagerRSV(IManagerRSV managerRSV) {
        this.managerRSV = managerRSV;
    }

    public void setLoginRSV(ILoginRSV loginRSV) {
        this.loginRSV = loginRSV;
    }

    public ManageLoginSuccessHandler(){
        
        executorService=Executors.newCachedThreadPool();
    }

    @Override
    public void successHandle(HttpServletRequest request) {
        AuthPrivilegeResDTO auth= WebContextUtil.getCurrentUser();
        String staffCode=request.getParameter("j_username");
        Long staffId = -1L;
        if (null != auth) {
            staffId = auth.getStaffId();
            final Long id=staffId;
            executorService.execute(new Runnable() {
                
                @Override
                public void run() {
                    managerRSV.loginSuccess(id);
                }
            });
            
        }

        final LoginAccessLogReqDTO loginAccessLogReqDTO = new LoginAccessLogReqDTO();
        loginAccessLogReqDTO.setCurrentSiteId(SiteLocaleUtil.getSite());
        loginAccessLogReqDTO.setInIp(InetTool.getClientAddr(request));
        loginAccessLogReqDTO.setLoginFlag("1");
        loginAccessLogReqDTO.setLoginFrom("1");
        loginAccessLogReqDTO.setOperateTime(new Timestamp(System.currentTimeMillis()));
        loginAccessLogReqDTO.setSessionId(request.getSession().getId());
        loginAccessLogReqDTO.setStaffId(staffId);
        loginAccessLogReqDTO.setStaffLoginName(staffCode);
        final LoginLogInfoReqDTO loginLogInfoReqDTO = new LoginLogInfoReqDTO();
        loginLogInfoReqDTO.setInIp(request.getRemoteAddr());
        loginLogInfoReqDTO.setCurrentSiteId(SiteLocaleUtil.getSite());
        loginLogInfoReqDTO.setSessionId(request.getSession().getId());
        loginLogInfoReqDTO.setStaffId(staffId);
        loginLogInfoReqDTO.setStaffLoginName(staffCode);
        loginLogInfoReqDTO.setInTime(new Timestamp(System.currentTimeMillis()));
//        authLoginRSV.insertLoginLog(loginAccessLogReqDTO, loginLogInfoReqDTO);
        
        executorService.execute(new Runnable() {
            
            @Override
            public void run() {
                loginRSV.insertLoginLog(loginAccessLogReqDTO, loginLogInfoReqDTO);
            }
        });
        
    }

}

