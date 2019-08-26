package com.zengshi.ecp.server.front.constans;

/**
 * Description:系统定义异常编码 <br>
 * Date: 2014年2月27日 <br>
 * 
 */
public class ExptCodeConstants {

    /**
     * Description: 特定异常<br>
     * Date: 2014年2月27日 <br>
     * 
     */
    public static class Special {
        // 系统级异常[其它系统级异常，未知异常]
        public static final String SYSTEM_ERROR_OTHER = "special.exception.other";   
        
        //未知的加密方法
        public static final String SYSTEM_ENCRY_UNKNOWN = "special.encry.unknow";
        
        //加密，入参，原文为空
        public static final String SYSTEM_ENCRY_NULL = "special.encry.null";
        
        public static final String SYSTEM_ENCRY_CHECK_NULL = "special.encry.check.null";
    }
    
}
