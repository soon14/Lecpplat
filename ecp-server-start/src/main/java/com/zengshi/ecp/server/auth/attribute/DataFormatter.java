package com.zengshi.ecp.server.auth.attribute;

import java.lang.annotation.*;

/**数据格式化设置
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.METHOD)
public @interface DataFormatter {
    String attrName();//属性名
    String attrValue() default "";//显示值
    String valueFmt() default "";//值显示格式，针对日期
    String spel() default "";//spring spel 表达式，表达式结果设置值
}
