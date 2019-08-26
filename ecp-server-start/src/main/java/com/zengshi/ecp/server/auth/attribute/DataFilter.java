package com.zengshi.ecp.server.auth.attribute;

import java.lang.annotation.*;

/**数据过滤
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface DataFilter {
    /**
     * 功能编码
     * @return
     */
    String funcCode();

    DataFormatter[] formatters() default {};
}
