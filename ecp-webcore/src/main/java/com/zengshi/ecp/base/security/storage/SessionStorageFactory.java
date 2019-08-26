/**
 * SessionStorageFactory.java	  V1.0   2013-4-28 下午4:32:20
 *
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.zengshi.ecp.base.security.storage;


import com.zengshi.ecp.server.front.security.AuthPrivilegeResDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * 功能描述：
 *
 *
 * 修改历史：(修改人，修改时间，修改原因/内容)
 */
public class SessionStorageFactory {

    private List<AbstractSessionStorage> storages;

    public void setStorages(List<AbstractSessionStorage> storages) {
    
        this.storages = storages;
    }
    
    public Map<String,Object> store(AuthPrivilegeResDTO authPrivilege){
        Map<String,Object> data=new HashMap<String,Object>();
        if(null==this.storages){
            return data;
        }
        for(AbstractSessionStorage storage : storages){
            Object value=storage.store(authPrivilege);
            data.put(storage.getKey(), value);
        }
        
        return data;
    }
}
