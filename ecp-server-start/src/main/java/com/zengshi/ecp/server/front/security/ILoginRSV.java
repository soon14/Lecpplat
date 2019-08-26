package com.zengshi.ecp.server.front.security;

/**
 */
public interface ILoginRSV {

    void updateOutLoginLog(LoginLogInfoReqDTO loginLogInfoReqDTO);

    void insertLoginLog(LoginAccessLogReqDTO loginAccessLogReqDTO, LoginLogInfoReqDTO loginLogInfoReqDTO);
}
