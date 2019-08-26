package com.zengshi.ecp.server.auth.attribute;

import java.util.List;

/**
 */
public abstract class AbstractRuleOfDataFilter implements IRuleOfDataFilter{
    /**
     *查询规则
     * @param funcCode  功能编码
     * @param staffId 人员id
     * @return 规则明细
     */
    public abstract List<FilterRule> getRules(String funcCode,Long staffId);


}
