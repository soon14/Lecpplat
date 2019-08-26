package com.zengshi.ecp.base.security.handler.support;

import com.zengshi.ecp.base.security.handler.ILoginSuccessHandler;
import com.zengshi.ecp.base.util.WebContextUtil;
import com.zengshi.ecp.server.front.exception.BusinessException;
import com.zengshi.ecp.server.front.security.IAuthRSV;
import com.zengshi.ecp.server.front.security.LoginPwdCntReqDTO;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 */
public class PasswordLoginSuccessHandler implements ILoginSuccessHandler {
    private final static Logger logger=Logger.getLogger(PasswordLoginSuccessHandler.class);

    private IAuthRSV authRSV;

    public void setAuthRSV(IAuthRSV authRSV) {
        this.authRSV = authRSV;
    }
    @Override
    public void successHandle(HttpServletRequest request) {

        if(null!=authRSV){
            String staffCode=WebContextUtil.getCurrentUser().getStaffCode();
            try {
                authRSV.updateLoginPwdCnt(new LoginPwdCntReqDTO(staffCode,"1"));
            } catch (BusinessException e) {
                logger.error("登录成功，更新密码次数。",e);
            }
        }

    }
}
