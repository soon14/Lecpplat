/** 
 * Date:2016年3月14日下午5:37:20 
 * 
 */ 
package com.zengshi.ecp.base.util;

/**
 * Description: 用于处理在密码验证的过程中，是否进行部分的忽略操作<br>
 * Date:2016年3月14日下午5:37:20  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class SecurityLocaleUtil {
    
    private static ThreadLocal<String> threadLocal = new ThreadLocal<String>();
    
    private static final String IGNORE_PASSWORD = "IGNORE";
    
    /**
     * 
     * setIgnore:设置忽略密码验证 <br/> 
     * 
     * @since JDK 1.6
     */
    public static void setPasswordIgnore(){
        threadLocal.set(IGNORE_PASSWORD);
    }
    
    /**
     * 
     * isIgnore: 判断是否忽略密码验证<br/> 
     * 
     * @return 
     * @since JDK 1.6
     */
    public static boolean isPasswordIgnore(){
        return IGNORE_PASSWORD.equalsIgnoreCase(threadLocal.get());
    }
    /**
     * 
     * isIgnore: 清除当前线程存储的变量 
     * 
     * @return 
     * @since JDK 1.6
     */
    public static void removePasswordIgnore(){
         threadLocal.remove();
    }
    
}

