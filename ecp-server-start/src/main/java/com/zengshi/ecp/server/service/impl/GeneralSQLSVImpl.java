/** 
 * File Name:GeneralSQLSVImpl.java 
 * Date:2015-8-12下午8:35:37 
 * 
 */ 
package com.zengshi.ecp.server.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import com.zengshi.ecp.frame.vo.BaseCriteria;
import com.zengshi.ecp.server.front.dto.BaseInfo;
import com.zengshi.ecp.server.front.dto.BaseResponseDTO;
import com.zengshi.ecp.server.front.dto.PageResponseDTO;
import com.zengshi.ecp.server.service.interfaces.IGeneralSQLSV;
import com.zengshi.ecp.server.service.pagination.PaginationCallback;

/**
 * Project Name:ecp-server-start <br>
 * Description: <br>
 * Date:2015-8-12下午8:35:37  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class GeneralSQLSVImpl implements IGeneralSQLSV {

    @Override
    public <T extends BaseResponseDTO, E> PageResponseDTO<T> queryByPagination(BaseInfo baseInfo,BaseCriteria criteria,
            boolean distributed,PaginationCallback<E, T> callback) {
        
        PageResponseDTO<T> pageResp=new PageResponseDTO<T>();
        
        int total=Long.valueOf(callback.queryTotal(criteria)).intValue();
        pageResp.setCount(total);
        if(baseInfo.getPageSize()==0){
            baseInfo.setPageSize(1);
        }
        int pageCount=total/baseInfo.getPageSize();
        pageCount=total%baseInfo.getPageSize()>0?pageCount+1:pageCount;
        pageResp.setPageCount(pageCount);
        pageResp.setPageNo(baseInfo.getPageNo());
        pageResp.setPageSize(baseInfo.getPageSize());
        if(0==total){
            return pageResp;
        }
        int start=criteria.getLimitClauseStart();
        if(distributed && callback.getTableCount()>0 && callback.calcRows()>0){//2017-01-09 jys 如果设置分表数量
            start=start/callback.getTableCount();
        }
        int count=criteria.getLimitClauseCount();
        int end=start+count;
        if(distributed && start>-1){
            int rows=callback.calcRows();
            int aStart=rows==0?0:(start-rows)<0?0:start-rows;
            criteria.setLimitClauseStart(aStart);
            int aEnd=aStart==0?end:(count+rows);
            criteria.setLimitClauseCount(aEnd);
        }
        List<E> listP=callback.queryDB(criteria);
        
        List<Comparator<E>> comparas=callback.defineComparators();
        if(null!=comparas){
            for(Comparator<E> comp : comparas){
                Collections.sort(listP, comp);
            }
        }
        List<E> subList=null;
        if(distributed && start>-1){
            int size=listP.size();
            end=size<end?size:end;
            int pstart=start;
            if(callback.calcRows()>0){
                pstart=start>size?size-count:start;//2017-01-09 jys 分库分表设置rows
            }
            if(pstart<end){
                subList=size<=baseInfo.getPageSize()?listP:listP.subList(pstart, end);
            }else{
                subList=new ArrayList<E>();
            }
        }else{
            subList=listP;
        }
        
        pageResp.setResult(new ArrayList<T>());
        for(E sub : subList){
            T t=callback.warpReturnObject(sub);
            pageResp.getResult().add(t);
        }
        return pageResp;
    }

}

