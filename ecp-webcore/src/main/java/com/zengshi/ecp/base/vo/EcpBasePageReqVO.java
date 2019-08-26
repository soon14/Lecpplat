/** 
 * Date:2015-8-11下午4:00:04 
 * 
 */ 
package com.zengshi.ecp.base.vo;

import com.zengshi.ecp.server.front.dto.BaseInfo;
import com.zengshi.paas.utils.LogUtil;

import java.io.Serializable;

/**
 * Description: <br>
 * Date:2015-8-11下午4:00:04  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class EcpBasePageReqVO implements Serializable {
    
    private static String MODULE = EcpBasePageReqVO.class.getName();

    /** 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = -569214730337813907L;
    //当前页；默认值为1；
    private int pageNumber = 1;
    //每页记录数，默认值为10；
    private int pageSize = 10;
    
    private String sortname;
    
    private String sortorder;
    
    private String query;
    
    private String qtype;


    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortname() {
        return sortname;
    }

    public void setSortname(String sortname) {
        this.sortname = sortname;
    }

    public String getSortorder() {
        return sortorder;
    }

    public void setSortorder(String sortorder) {
        this.sortorder = sortorder;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQtype() {
        return qtype;
    }

    public void setQtype(String qtype) {
        this.qtype = qtype;
    }
    
    /**
     * 
     * toBaseInfo:转为后场的查询条件 <br/>      * 
     * @param vo
     * @return 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @since JDK 1.6
     */
    public <T extends BaseInfo> T toBaseInfo(Class<T> clazz){
        T t = null;
        try{
            t = clazz.newInstance();
        } catch(Exception err){
            LogUtil.error(MODULE, "生成："+clazz.getName()+"的对象失败，检查类是否加载，并且是否提供无参数构造函数");
            return null;
        }
        t.setPageNo(this.getPageNumber());
        t.setPageSize(this.getPageSize());
        t.setGridQuerySortName(this.sortname);
        t.setGridQuerySortOrder(this.sortorder);
        return t;
    }
    
}

