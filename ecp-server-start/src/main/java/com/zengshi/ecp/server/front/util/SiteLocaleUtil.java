/** 
 * File Name:SiteLocaleUtil.java 
 * Date:2015-9-25上午9:33:12 
 * 
 */ 
package com.zengshi.ecp.server.front.util;

/**
 * Project Name:ecp-server-start <br>
 * Description: 站点的线程变量 <br>
 * Date:2015-9-25上午9:33:12  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class SiteLocaleUtil {
    
    private static ThreadLocal<Long> siteLocale = new ThreadLocal<Long>();

    public static Long getSite() {
        Long site =  siteLocale.get();
        if(site == null){
            return 0L;
        } else {
            return site;
        }
    }

    public static void setSite(Long site) {
       siteLocale.set(site);
    }
    
}

