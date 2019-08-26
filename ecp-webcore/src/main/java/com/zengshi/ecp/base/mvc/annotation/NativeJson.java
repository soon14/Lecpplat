/** 
 * Date:2015年8月31日下午7:53:20 
 * 
*/
package com.zengshi.ecp.base.mvc.annotation;

import java.lang.annotation.*;

/** 
 * Description: json返回值不做封装<br>
 * Date:2015年8月31日下午7:53:20  <br>
 * 
 * @since JDK 1.6 
 * @see       
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface NativeJson {

    public boolean value() default true;
}

