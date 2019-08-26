/**
 * DefaultSessionStore.java	  V1.0   2013-4-22 下午5:45:40
 *
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.zengshi.ecp.base.security.storage.support;

import com.zengshi.ecp.base.security.storage.AbstractSessionStorage;
import com.zengshi.ecp.server.front.security.AuthPrivilegeResDTO;


public class DefaultSessionStorage extends AbstractSessionStorage{

    public final static String STORAGE_KEY="default";

    @Override
    public Object store(AuthPrivilegeResDTO authPrivilege) {

        return authPrivilege;
    }

    @Override
    public String getKey() {

        return STORAGE_KEY;
    }

}
