/** 
 * Date:2015年8月11日下午2:33:04 
 * 
*/
package com.zengshi.ecp.base.util;

/** 
 * Description: <br>
 * Date:2015年8月11日下午2:33:04  <br>
 * 
 * @since JDK 1.6 
 * @see       
 */
public class ParamConstant {
    /**
     * ajax请求头参数名
     */
    public final static String AJAX_REQUEST_HEAD_KEY="X-Requested-With";
    /**
     * ajax请求头值
     */
    public final static String AJAX_REQUEST_HEAD_VALUE="XMLHttpRequest";
    /**
     * session超时请求头参数名
     */
    public final static String SESSION_TIMEOUT_HEAD_KEY="sessionstatus";
    /**
     * session超时请求头值
     */
    public final static String SESSION_TIMEOUT_HEAD_VALUE="timeout";
    
    /**
     * 登录验证码
     */
    public final static String LOGIN_CHECK_CODE="j_check_code";
    /**
     * 登录用户名
     */
    public final static String LOGIN_USERNAME_KEY="j_username";
    /**
     * 登录密码
     */
    public final static String LOGIN_PASSWORD_KEY="j_password";
    /**
     * 登录短信验证码
     */
    public final static String LOGIN_SMS_CODE="j_sms_code";
    /**
     * 匿名访问权限
     */
    public final static String AUTHORITY_ROLE_ANONYMOUS="ROLE_ANONYMOUS";
    /**
     * 公共访问权限
     */
    public final static String AUTHORITY_ROLE_PUBLIC="ROLE_PUBLIC";
    /**
     * 默认权限
     */
    public final static String AUTHORITY_ROLE_DEFAULT="ROLE_USER";
    /**
     * 超级权限
     */
    public final static String AUTHORITY_ROLE_SUPER="ROLE_SUPER";
    /**
     * 权限名前缀
     */
    public final static String AUTHORITY_ROLE_PREFIX="ROLE_";
    /**
     * 登录成功标识
     */
    public final static String LOGIN_FLAG_KEY="login_success";

    /**
     * cookie上的userId
     */
    public static final String CK_USER_ID = "cksid";

    /**
     * 表示登录的验证来源
     */
    public static final String LOGIN_SSO_KEY="j_from";

    /**
     * 表示登录是来自于SSO
     */
    public static final String LOGIN_SSO_VALUE="SSO";
    /**
     * 表示登录是来自于SMS
     */
    public static final String LOGIN_SMS_VALUE="SMS";
    /**
     * 获取短信验证码前缀
     */
    public static final String PHONE_CHECK_PRE = "PHONE_CHECK_";
    /**
     * 用户锁定操作前缀
     */
    public static final String USER_LOCK = "USER_LOCK_";
    
}

