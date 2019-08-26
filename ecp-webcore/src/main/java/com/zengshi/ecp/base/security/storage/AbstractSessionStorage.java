/**
 * AbstactSessionStorage.java	  V1.0   2013-4-22 下午5:40:41
 *
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.zengshi.ecp.base.security.storage;


import com.zengshi.ecp.server.front.security.AuthPrivilegeResDTO;

/**
 * 
 * 功能描述：用户session存储器
 *
 *
 * 修改历史：(修改人，修改时间，修改原因/内容)
 */
public abstract class AbstractSessionStorage {
    /**
     * 
     * 功能描述：存储执行
     *
     * 创建日期 ：2013-4-22 下午5:44:02
     *
     * @param authPrivilege 登录用户
     * @return 存储数据
     *
     * 修改历史 ：(修改人，修改时间，修改原因/内容)
     */
    public abstract Object store(AuthPrivilegeResDTO authPrivilege);
    /**
     * 
     * 功能描述：存储值对应的key
     *
     * 创建日期 ：2013-4-28 下午4:47:30
     *
     * @return
     *
     * 修改历史 ：(修改人，修改时间，修改原因/内容)
     */
    public abstract String getKey();
}
