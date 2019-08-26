package com.zengshi.ecp.server.auth;

import com.zengshi.ecp.frame.vo.BaseCriteria;

import java.lang.annotation.*;

/**数据权限验证标识
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface DataAuthValid {
    /**
     * 数据对象命名，多个数据对象以逗号分隔
     * @return
     */
    String value() default "";
    /**
     * 功能编码
     * @return
     */
    String funcCode();

    /**
     *未配置权限用户是否拦截
     * @return
     */
    boolean isIntercept() default false;

    /**
     * 权限验证类型
     * @return
     */
    DataAuthType authType() default DataAuthType.BIND;

    /**
     * Criteria对象类
     * @return
     */
    Class<? extends BaseCriteria> criteriaClass() default BaseCriteria.class;
}
