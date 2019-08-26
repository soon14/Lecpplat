package com.zengshi.ecp.server.auth;

import com.zengshi.ecp.server.front.util.StaffLocaleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**数据权限规则获取类
 */
public class RuleOfDataAuthUtil {

    private static IRuleOfDataAuth ruleOfDataAuth;

//    @Resource
    @Autowired(required = false)
//    @Qualifier
    public void setRuleOfDataAuth(IRuleOfDataAuth ruleOfDataAuth) {
        RuleOfDataAuthUtil.ruleOfDataAuth = ruleOfDataAuth;
    }

    public static IRuleOfDataAuth getRuleOfDataAuth() {
        return ruleOfDataAuth;
    }

    /**
     * 获取当前用户指定功能的数据规则
     * @param funcCode
     * @return
     */
    public static String getFragmentOfSql(String funcCode){
        return getFragmentOfSql(funcCode, StaffLocaleUtil.getStaff().getId());
    }

    /**
     *获取指定用户下指定功能的规则(sql代码段)
     * @param funcCode 功能编码
     * @param staffId 用户ID
     * @return
     */
    public static String getFragmentOfSql(String funcCode,long staffId){
        return ruleOfDataAuth.getFragmentOfSql(funcCode,staffId);//178
    }

    /**
     * 获取指定用户目录下指定功能的规则(sql代码段)mybatis格式及入参值
     * @param funcCode 功能编码
     * @param staffId 用户ID
     * @param sqlAlias sql代码段别名
     * @return
     */
    public static Map<String,Object> getFramgmentOfSql(String funcCode,long staffId,String sqlAlias){

        return ruleOfDataAuth.getFramgmentOfSql(funcCode, staffId, sqlAlias);
    }

    /**
     * 获取当前用户目录下指定功能的规则(sql代码段)mybatis格式及入参值
     * @param funcCode 功能编码
     * @param sqlAlias sql代码段别名
     * @return
     */
    public static Map<String,Object> getFramgmentOfSql(String funcCode,String sqlAlias){

        return getFramgmentOfSql(funcCode, StaffLocaleUtil.getStaff().getId(), sqlAlias);
    }

    /**
     * 获取当前用户目录下指定功能的规则(sql代码段)mybatis格式及入参值
     * @param funcCode 功能编码
     * @return
     */
    public static Map<String,Object> getFramgmentOfSql(String funcCode){

        return getFramgmentOfSql(funcCode, StaffLocaleUtil.getStaff().getId(),null);
    }
    /**
     * 判断指定用户是否配置指定功能的数据权限
     * @param funcCode
     * @param staffId 用户ID
     * @return
     */
    public static boolean judgeAuthOfCurrentUser(String funcCode,long staffId){
        return ruleOfDataAuth.judgeAuthOfCurrentUser(funcCode,staffId);
    }

    /**
     * 判断当前用户是否配置指定功能的数据权限
     * @param funcCode
     * @return
     */
    public static boolean judgeAuthOfCurrentUser(String funcCode){
        return judgeAuthOfCurrentUser(funcCode,StaffLocaleUtil.getStaff().getId());
    }

    /**
     * 获取指定用户下指定功能的规则(逻辑表达式)
     * @param funcCode 功能编码
     * @param staffId 用户ID
     * @return
     */
    public static List<String> getRule(String funcCode,long staffId){
        return ruleOfDataAuth.getRule(funcCode,staffId);
    }

    /**
     * 获取当前用户下指定功能的规则(逻辑表达式)
     * @param funcCode 功能编码
     * @return
     */
    public static List<String> getRule(String funcCode){
        return getRule(funcCode,StaffLocaleUtil.getStaff().getId());
    }

    /**
     *获取指定用户下指定功能的规则(属性、值)
     * @param funcCode 功能编码
     * @param staffId 用户ID
     * @return key :属性名  value: 属性值
     */
    public static Map<String,Object> getRuleProperties(String funcCode,long staffId){
        return ruleOfDataAuth.getRuleProperties(funcCode,staffId);
    }

    /**
     *获取当前用户下指定功能的规则(属性、值)
     * @param funcCode 功能编码
     * @return key :属性名  value: 属性值
     */
    public static Map<String,Object> getRuleProperties(String funcCode){
        return getRuleProperties(funcCode,StaffLocaleUtil.getStaff().getId());
    }
    /**
     *获取指定用户下指定功能的规则项目集合
     * @param funcCode 功能编码
     * @param staffId 用户ID
     * @return com.zengshi.ecp.server.auth.RuleObject类对象
     */
    public static List<RuleObject> getRuleObjects(String funcCode,long staffId){
        return ruleOfDataAuth.getRuleObjects(funcCode,staffId);
    }
    /**
    *获取当前用户下指定功能的规则项目集合
    * @param funcCode 功能编码
    * @return com.zengshi.ecp.server.auth.RuleObject类对象
    */
    public static List<RuleObject> getRuleObjects(String funcCode){
        return getRuleObjects(funcCode,StaffLocaleUtil.getStaff().getId());
    }
}
