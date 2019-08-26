package com.zengshi.ecp.server.auth;

import java.lang.annotation.*;

/**值变量对象标识
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface VarObject {
    /**
     *变量对象命名
     * @return
     */
    String value() default "";
}
