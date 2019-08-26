package com.zengshi.ecp.server.auth;

import java.util.List;
import java.util.Map;

/**数据权限规则
 */
public interface IRuleOfDataAuth {
    /**
     * 获取指定用户下指定功能的规则(逻辑表达式)
     * @param funcCode 功能编码
     * @param staffId 用户ID
     * @return
     */
    List<String> getRule(String funcCode,long staffId);

    /**
     *获取指定用户下指定功能的规则(属性、值)
     * @param funcCode 功能编码
     * @param staffId 用户ID
     * @return key :属性名  value: 属性值
     */
    Map<String,Object> getRuleProperties(String funcCode,long staffId);

    /**
     *获取指定用户下指定功能的规则(sql代码段)包括值
     * @param funcCode 功能编码
     * @param staffId 用户ID
     * @return
     */
    String getFragmentOfSql(String funcCode,long staffId);

    /**
     * 获取指定用户目录下指定功能的规则(sql代码段)mybatis格式及入参值
     * @param funcCode 功能编码
     * @param staffId 用户ID
     * @param sqlAlias sql代码段别名
     * @return
     */
    Map<String,Object> getFramgmentOfSql(String funcCode,long staffId,String sqlAlias);

    /**
     *获取指定用户下指定功能的规则项目集合
     * @param funcCode 功能编码
     * @param staffId 用户ID
     * @return com.zengshi.ecp.server.auth.RuleObject类对象
     */
    List<RuleObject> getRuleObjects(String funcCode,long staffId);

    /**
     * 判断指定用户是否配置指定功能的数据权限
     * @param funcCode
     * @return
     */
    boolean judgeAuthOfCurrentUser(String funcCode,long staffId);
}
