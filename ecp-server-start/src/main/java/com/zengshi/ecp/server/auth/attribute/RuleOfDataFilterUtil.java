package com.zengshi.ecp.server.auth.attribute;

import com.zengshi.ecp.server.front.util.StaffLocaleUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;

/**
 */
public class RuleOfDataFilterUtil {

    private static IRuleOfDataFilter ruleOfDataFilter;

    @Autowired(required = false)
    public void setRuleOfDataFilter(IRuleOfDataFilter ruleOfDataFilter) {
        RuleOfDataFilterUtil.ruleOfDataFilter = ruleOfDataFilter;
    }

    public static boolean isExistOfRuleFilter(){

        return RuleOfDataFilterUtil.ruleOfDataFilter!=null;
    }

    public static List<FilterRule> getRules(String funcCode){

        return getRules(funcCode, StaffLocaleUtil.getStaff().getId());
    }

    public static List<FilterRule> getRules(String funcCode,Long staffId){

        return ruleOfDataFilter.getRules(funcCode, staffId);
    }
}
