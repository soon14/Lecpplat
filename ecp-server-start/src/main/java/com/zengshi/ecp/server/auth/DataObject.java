package com.zengshi.ecp.server.auth;

import java.lang.annotation.*;

/**数据对象标识
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface DataObject {
    /**
     * 对象命名
     * @return
     */
    String value() default "";

    /**
     * 用于规则的属性
     * @return
     */
    String[] include() default {};

    /**
     * 排除规则的属性
     * @return
     */
    String[] exclude() default {};
}
