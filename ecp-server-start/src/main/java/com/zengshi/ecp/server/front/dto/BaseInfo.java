/**
 * 
 */
package com.zengshi.ecp.server.front.dto;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Locale;

import com.zengshi.ecp.server.front.util.SiteLocaleUtil;
import com.zengshi.ecp.server.front.util.StaffLocaleUtil;
import com.zengshi.paas.common.ICriteria;
import com.zengshi.paas.utils.LocaleUtil;
import com.zengshi.paas.utils.ThreadId;
import com.alibaba.fastjson.JSON;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.record.GridsetRecord;

/**
 * 
 * Project Name:ecp-server-start <br>
 * Description: 外部调用必须传入的参数的基类 <br>
 * Date:2015-7-21下午4:24:40  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public class BaseInfo<T extends ICriteria> implements Serializable{
	
    private static final long serialVersionUID = 7849408112382193366L;
    private static final Logger logger=Logger.getLogger(BaseInfo.class);
    
    private Integer pageNo = 1;// 请求查询的页码

    private Integer pageSize = 1;// 每页显示条数
    
    private String gridQuerySortName = null;//排序字段名
    
    private String gridQuerySortOrder = null;//排列顺序，若存在sortName而不传递sortOrder，则默认使用asc方式进行正序排序
    
    private String appName = "ai-ecp"; // 来源系统
    
    private String threadId = "";
    
    private Locale locale = null;
    
    ///基础用户资料
    private BaseStaff staff = null;
    //查询条件对象
    private T criteria=null;
    private Class<T> entityClass;

    //站点ID，表示当前操作的是那个站点，由前店传入；前店通过对URL进行解析处理；
    private Long currentSiteId = 0L;// 站点信息；

    /**
	 * 
	 * Creates a new instance of BaseInfo. 
	 *
	 */
	public BaseInfo(){
	    
		threadId = ThreadId.getThreadId();
		locale = LocaleUtil.getLocale();
		staff = StaffLocaleUtil.getStaff();
		currentSiteId = SiteLocaleUtil.getSite();

        Type genType = getClass().getGenericSuperclass();
        if(ParameterizedType.class.isAssignableFrom(genType.getClass())){
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            entityClass = (Class) params[0];
        }
    }
	
	public BaseStaff getStaff() {
        return staff;
    }

    public void setStaff(BaseStaff staff) {
        this.staff = staff;
    }

    /**
     * 获取开始行
     *   取的是上一页的结束行；从0开始；
     *   例如：第2页，每页10条记录，那么本页的起始记录：（2-1）×10 ；
     * @return
     */
    public int getStartRowIndex() {
        return (this.getPageNo() - 1) * this.getPageSize();
    }

    /**
     * 获取本页结束行 
     * 例如：第2页，每页10条记录，那么本页的结束记录：2×10 ；
     * @return
     */
    public int getEndRowIndex() {
        return this.getPageNo() * this.getPageSize();
    }
    
	public String getThreadId(){
		return threadId;
	}
	
	public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Long getCurrentSiteId() {
        return currentSiteId;
    }

    public void setCurrentSiteId(Long currentSiteId) {
        this.currentSiteId = currentSiteId;
    }

    public String getGridQuerySortName() {
		return gridQuerySortName;
	}

	public void setGridQuerySortName(String gridQuerySortName) {
		this.gridQuerySortName = gridQuerySortName;
	}

	public String getGridQuerySortOrder() {
		return gridQuerySortOrder;
	}

	public void setGridQuerySortOrder(String gridQuerySortOrder) {
		this.gridQuerySortOrder = gridQuerySortOrder;
	}

	public T getCriteria(){
        if(null != entityClass && null==criteria){
            try {
                if(null==criteria){
                    criteria = entityClass.newInstance();
                }
                if(StringUtils.isNotEmpty(gridQuerySortName) && criteria!=null){
                	String sOrder = StringUtils.isNotEmpty(gridQuerySortOrder) ? gridQuerySortOrder.trim() : "asc";
                	String orderStr = " " + gridQuerySortName.trim() + " " + sOrder;
                	//criteria.setOrderByClause(orderStr);
                	//反射调用
                	Method m = entityClass.getMethod("setOrderByClause");
                	m.invoke(orderStr);
                }
            } catch (Exception e) {
                logger.info("查询条件对象实例化失败。"+entityClass.getName());
            }
        }
        return criteria;
    }

    public void setCriteria(T criteria) {
        this.criteria = criteria;
    }
}
