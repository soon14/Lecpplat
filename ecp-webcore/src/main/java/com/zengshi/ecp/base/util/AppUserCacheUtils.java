/** 
 * Date:2016年3月12日下午5:15:46 
 * 
 */ 
package com.zengshi.ecp.base.util;

import com.zengshi.ecp.server.front.dto.BaseStaff;
import com.zengshi.paas.utils.CacheUtil;

import java.util.UUID;

/**
 * Description: <br>
 * Date:2016年3月12日下午5:15:46  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class AppUserCacheUtils {
    
    private static final String USER_CACHE_PREFIX = "APP_USER_";
    
    private static final int ACTIVE_TIME = 3600*24*30;
    
    
    /**
     * 
     * saveUser2Cache: 将用户信息保存在缓存中，并返回一个tocken <br/> 
     * 
     * @param staffInfo
     * @return 
     * @since JDK 1.6
     */
    public static String saveUser2Cache(BaseStaff staffInfo){
        
        String tocken = UUID.randomUUID().toString().replaceAll("-", "");
        
        CacheUtil.addItem(USER_CACHE_PREFIX+tocken, staffInfo, ACTIVE_TIME);
        
        return tocken;
    }
    
    /**
     * 
     * fetchUser:通过一个存储的tocken 获取 保存在缓存中的 用户信息 <br/> 
     * 
     * @param tocken
     * @return 
     * @since JDK 1.6
     */
    public static BaseStaff fetchUser(String tocken){
        
        Object obj = CacheUtil.getItem(USER_CACHE_PREFIX+tocken);
        if(obj == null){
            return null;
        } else {
            return (BaseStaff)obj;
        }
        
    }
    
    /**
     * 
     * removeUserFromCache: 从缓存中移除保存的BaseStaff<br/> 
     * 
     * @param tocken 
     * @since JDK 1.6
     */
    public static void removeUserFromCache(String tocken){
        
        CacheUtil.delItem(USER_CACHE_PREFIX+tocken);
        
    }

}

