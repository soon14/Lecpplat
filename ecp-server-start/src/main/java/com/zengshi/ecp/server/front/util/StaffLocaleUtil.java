/** 
 * File Name:StaffLocaleUtil.java 
 * Date:2015-8-26下午4:14:03 
 * 
 */ 
package com.zengshi.ecp.server.front.util;

import com.zengshi.ecp.server.front.dto.BaseStaff;

/**
 * Project Name:ecp-server-start <br>
 * Description: 用于登录用户的线程变量；用于登录的时候获取；
 *    new BaseInfo 的时候，BaseInfo的值，从这里获取；<br>
 * Date:2015-8-26下午4:14:03  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class StaffLocaleUtil {
    
    private static ThreadLocal<BaseStaff> threadLocal = new ThreadLocal<BaseStaff>();
    
    public static BaseStaff getStaff(){
        BaseStaff staff = threadLocal.get();
        if(staff == null){
            return new BaseStaff();
        } else {
            return staff;
        }
    }
    
    public static void setStaff(BaseStaff staff){
        threadLocal.set(staff);
    }
    
}

