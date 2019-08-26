package com.zengshi.ecp.server.front.security;

import java.io.Serializable;

/**用户登录密码错误次数入参
 */
public class LoginPwdCntReqDTO implements Serializable{
    private static final long serialVersionUID = 3181570890162613680L;
    //用户登录名
    private String staffLoginName;
    //1：密码正确，0：密码错误
    private String loginFlag;

    public LoginPwdCntReqDTO(String staffLoginName, String loginFlag) {
        this.staffLoginName = staffLoginName;
        this.loginFlag = loginFlag;
    }

    public String getStaffLoginName() {
        return staffLoginName;
    }

    public void setStaffLoginName(String staffLoginName) {
        this.staffLoginName = staffLoginName;
    }

    public String getLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(String loginFlag) {
        this.loginFlag = loginFlag;
    }
}
