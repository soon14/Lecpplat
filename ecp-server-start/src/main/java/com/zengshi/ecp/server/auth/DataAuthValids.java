package com.zengshi.ecp.server.auth;

import java.lang.annotation.*;

/**多数据规则验证
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface DataAuthValids {
    DataAuthValid[] value();
}
