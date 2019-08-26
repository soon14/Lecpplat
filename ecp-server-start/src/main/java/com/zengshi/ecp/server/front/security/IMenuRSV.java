package com.zengshi.ecp.server.front.security;

import java.util.List;

/**
 */
public interface IMenuRSV {

    List<AuthMenuResDTO> listAuthMenu(AuthManageReqDTO dto);
}
