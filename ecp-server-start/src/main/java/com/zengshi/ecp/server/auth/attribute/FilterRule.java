package com.zengshi.ecp.server.auth.attribute;

import java.io.Serializable;

/**过滤规则
 */
public class FilterRule implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -173396063371635051L;
	
    /**
     *属性名
     */
    private String name;
    /**
     *数据类型
     */
    private String clazz;
    /**
     * 数据格式
     */
    private String formatter;
    /**
     * 显示值
     */
    private String value;

    public FilterRule(String name,String clazz,String formatter,String value){
        this.name=name;
        this.clazz=clazz;
        this.formatter=formatter;
        this.value=value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getFormatter() {
        return formatter;
    }

    public void setFormatter(String formatter) {
        this.formatter = formatter;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FilterRule that = (FilterRule) o;

        if (!name.equals(that.name)) return false;
        if (!clazz.equals(that.clazz)) return false;
        if (formatter != null && !formatter.equals(that.formatter)) return false;
        if (value != null && !value.equals(that.value)) return false;
        return true;

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + clazz.hashCode();
        if(formatter != null) result = 31 * result + formatter.hashCode();
        if(value != null) result = 31 * result + value.hashCode();
        return result;
    }
}
