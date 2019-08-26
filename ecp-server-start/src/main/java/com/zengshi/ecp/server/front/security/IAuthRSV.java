package com.zengshi.ecp.server.front.security;

import com.zengshi.ecp.server.front.exception.BusinessException;

/**用户权限接口服务
 */
public interface IAuthRSV {

    AuthPrivilegeResDTO findPrivilByStaffCode(String username, String staffClass);

    /**
     * 用户登录密码错误次数
     * @return
     * @throws BusinessException
     */
    int updateLoginPwdCnt(LoginPwdCntReqDTO dto) throws BusinessException;
}
