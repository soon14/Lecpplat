package com.zengshi.ecp.server.auth;

/**sql操作符
 */
public enum OperatorChar {
    IsNull("IsNull","is null"),IsNotNull("IsNotNull","is not null"),EqualTo("EqualTo","="),NotEqualTo("NotEqualTo","!="),
    GreaterThan("GreaterThan",">"), GreaterThanOrEqualTo("GreaterThanOrEqualTo",">="),LessThan("LessThan","<"),
    LessThanOrEqualTo("LessThanOrEqualTo","<="), In("In","in"),NotIn("NotIn","not in"),
    Between("Between","between"),NotBetween("NotBetween","not between"),Like("Like","like"),NotLike("NotLike","not like");

    private String value;
    private String operate;
    private OperatorChar(String value,String operate){
        this.value=value;
        this.operate=operate;
    }

    public String getValue() {
        return value;
    }

    public String getOperate() {
        return operate;
    }
}
