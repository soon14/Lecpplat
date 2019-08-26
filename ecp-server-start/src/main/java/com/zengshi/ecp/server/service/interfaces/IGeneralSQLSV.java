/** 
 * File Name:IGeneralSQLSV.java 
 * Date:2015-8-12下午8:32:19 
 * 
 */ 
package com.zengshi.ecp.server.service.interfaces;


import com.zengshi.ecp.frame.vo.BaseCriteria;
import com.zengshi.ecp.server.front.dto.BaseInfo;
import com.zengshi.ecp.server.front.dto.BaseResponseDTO;
import com.zengshi.ecp.server.front.dto.PageResponseDTO;
import com.zengshi.ecp.server.service.pagination.PaginationCallback;

/**
 * Project Name:ecp-server-start <br>
 * Description: <br>
 * Date:2015-8-12下午8:32:19  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public interface IGeneralSQLSV {
    
    /**
     * 
     * queryByPagination:分页查询通用方法. <br/> 
     * 
     * 
     * @param baseInfo 分页请求对象
     * @param criteria 分页请求对象
     * @param distributed true:分库分表查询  false:单库单表查询
     * @param callback com.zengshi.ecp.server.service.pagination.PaginationCallback类回调对象
     * @return PageResponseDTO分页对象
     * @since JDK 1.6
     */
    <T extends BaseResponseDTO,E> PageResponseDTO<T> queryByPagination(BaseInfo baseInfo,BaseCriteria criteria,
            boolean distributed,PaginationCallback<E,T> callback);
}

