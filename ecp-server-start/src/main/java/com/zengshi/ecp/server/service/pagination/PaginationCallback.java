/** 
 * File Name:PaginationWrap.java 
 * Date:2015年8月13日上午10:12:55 
 * 
*/
package com.zengshi.ecp.server.service.pagination;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.zengshi.ecp.frame.vo.BaseCriteria;
import com.db.distribute.DistributeRuleAssist;
import com.db.distribute.DistributedStatement;


/** 
 * Project Name:ecpframe <br>
 * Description: 分页查询类<br>
 * Date:2015年8月13日上午10:12:55  <br>
 * 
 * @since JDK 1.6 
 * @see       
 */
public abstract class PaginationCallback<T,E> {
    
    /**
     * 
     * queryDB:查询数据库的回调方法. <br/> 
     *  
     * 
     * @param criteria
     * @return 
     * @since JDK 1.6
     */
    public abstract  List<T> queryDB(BaseCriteria criteria);
    /**
     * 
     * queryTotal:查询总记录数 <br/> 
     * 
     * @param criteria
     * @return 
     * @since JDK 1.6
     */
    public abstract long queryTotal(BaseCriteria criteria);
    
    /**
     * 
     * defineComparators:定义排序用的比较器. <br/> 
     * 
     * 
     * @return 
     * @since JDK 1.6
     */
    public List<Comparator<T>> defineComparators(){
        
        return null;
    }
    /**
     * 
     * calcRows:设置每个表查询结果集数. <br/> 
     * 
     * @return 
     * @since JDK 1.6
     */
    public int calcRows(){
        
        return 0;
    }

    /**
     * 设置分表数(数据平分在各张表时设置)
     * @return
     */
    public int getTableCount(){

        return 0;
    }
    /**
     * 
     * warpReturnObject:自定义查询结果对象与返回对象之间的转换. <br/> 
     * 
     * 
     * @param t 数据库查询结果对象
     * @since JDK 1.6
     */
    public abstract E warpReturnObject(T t);
}

