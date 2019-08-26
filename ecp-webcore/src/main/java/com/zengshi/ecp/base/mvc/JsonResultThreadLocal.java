/** 
 * Date:2015年8月31日下午8:03:40 
 * 
*/
package com.zengshi.ecp.base.mvc;

/** 
 * Description: 存储是否需要封装json对象<br>
 * Date:2015年8月31日下午8:03:40  <br>
 * 
 * @since JDK 1.6 
 * @see       
 */
public class JsonResultThreadLocal {

    private static ThreadLocal<Boolean> isWarpLocal=new ThreadLocal<Boolean>(){

        @Override
        protected Boolean initialValue() {
            
            return Boolean.TRUE;
        }
        
    };
    
    public  static void set(boolean isWarp){
        
        isWarpLocal.set(isWarp);
    }
    
    public static boolean get(){
        
        return isWarpLocal.get()==null?true:isWarpLocal.get();
    }
}

