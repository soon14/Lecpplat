package com.zengshi.ecp.server.auth.attribute;

import java.util.List;

/**
 */
public interface IRuleOfDataFilter {

    List<FilterRule> getRules(String funcCode, Long staffId);
}
