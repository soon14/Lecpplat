package com.zengshi.ecp.server.auth;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**规则对象
 */
public class RuleObject implements Comparable<RuleObject>,Serializable{
    private static final long serialVersionUID = 5525585078764288629L;
    private static Map<String,OperatorChar> opMap=new ConcurrentHashMap<String,OperatorChar>();
    static {
        opMap.put("=", OperatorChar.EqualTo);
        opMap.put("==", OperatorChar.EqualTo);
        opMap.put("&eq;", OperatorChar.EqualTo);
        opMap.put("&ge;",OperatorChar.GreaterThanOrEqualTo);
        opMap.put(">=",OperatorChar.GreaterThanOrEqualTo);
        opMap.put("&le;",OperatorChar.LessThanOrEqualTo);
        opMap.put("<=",OperatorChar.LessThanOrEqualTo);
        opMap.put("&ne;",OperatorChar.NotEqualTo);
        opMap.put("!=",OperatorChar.NotEqualTo);
        opMap.put("&gt;",OperatorChar.GreaterThan);
        opMap.put(">",OperatorChar.GreaterThan);
        opMap.put("&lt;",OperatorChar.LessThan);
        opMap.put("<",OperatorChar.LessThan);
        opMap.put("isNull",OperatorChar.IsNull);
        opMap.put("isNotNull",OperatorChar.IsNotNull);
        opMap.put("like",OperatorChar.Like);
        opMap.put("notLike",OperatorChar.NotLike);
        opMap.put("in",OperatorChar.In);
        opMap.put("notIn",OperatorChar.NotIn);
        opMap.put("between",OperatorChar.Between);
        opMap.put("notBetween",OperatorChar.NotBetween);
    }
    /**
     * 属性名
     */
    private String name;
    /**
     * 字段名
     */
    private String field;
    /**
     * 操作符
     */
    private String op;
    /**
     * 值对象
     */
    private Object value;
    /**
     * 是否逻辑或
     */
    private boolean isOr=false;
    /**
     * 有左括号
     */
    private boolean hasLeft=false;
    /**
     * 有右括号
     */
    private boolean hasRight=false;

    /**
     * 常规规则项目
     * @param name 属性名
     * @param op 操作符
     * @param value 值
     */
    public RuleObject(String name,String op,Object value){
        this.name=name;
        this.op=op;
        this.value=value;
    }

    /**
     *规则项目（有数据库表字段）
     * @param name
     * @param field
     * @param op
     * @param value
     */
    public RuleObject(String name,String field,String op,Object value){
        this(name,op,value);
        this.field=field;
    }

    /**
     * 完整规则项目
     * @param name 属性名
     * @param field 表字段名
     * @param op 操作符
     * @param value 值
     * @param isOr 是否逻辑或
     * @param hasLeft 有左括号
     * @param hasRight 有右括号
     */
    public RuleObject(String name,String field,String op,Object value,boolean isOr,boolean hasLeft,boolean hasRight){
        this(name,field,op,value);
        this.isOr=isOr;
        this.hasLeft=hasLeft;
        this.hasRight=hasRight;
    }

    /**
     * 属性名
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * 操作符
     * @return
     */
    public OperatorChar getOp() {
        return opMap.get(op);
    }

    /**
     *项目值
     * @return
     */
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * 是否逻辑或，默认逻辑与
     * @return
     */
    public boolean isOr() {
        return isOr;
    }

    public void setIsOr(boolean isOr) {
        this.isOr = isOr;
    }

    /**
     * 是否有左口号
     * @return
     */
    public boolean isHasLeft() {
        return hasLeft;
    }

    /**
     * 是否有右括号
     * @return
     */
    public boolean isHasRight() {
        return hasRight;
    }

    /**
     * 数据库表字段名
     * @return
     */
    public String getField() {
        return field;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17,37)
                .append(this.field)
                .append(this.name)
                .append(this.value)
                .append(this.op)
                .append(this.hasLeft)
                .append(this.hasRight)
                .append(this.isOr)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(null==obj){
            return false;
        }
        if(getClass()!=obj.getClass()){
            return false;
        }
        RuleObject target=(RuleObject)obj;

        return new EqualsBuilder()
                .append(this.name,target.name)
                .append(this.field,target.field)
                .append(this.op,target.op)
                .append(this.value,target.value)
                .append(this.isHasLeft(),target.isHasLeft())
                .append(this.isHasRight(),target.isHasRight())
                .append(this.isOr,target.isOr)
                .isEquals();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int compareTo(RuleObject o) {
        return CompareToBuilder.reflectionCompare(this,o);
    }
}
