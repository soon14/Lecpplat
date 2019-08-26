/** 
 * File Name:IBaseAreaAdminRSV.java 
 * Date:2015-8-24下午9:35:07 
 * 
 */ 
package com.zengshi.ecp.server.front.param;

import com.zengshi.ecp.server.front.dto.BaseAreaAdminReqDTO;
import com.zengshi.ecp.server.front.dto.BaseAreaAdminRespDTO;

import java.util.List;


/**
 * Project Name:ecp-services-sys <br>
 * Description: 与区域数据相关的Dubbo服务<br>
 * Date:2015-8-24下午9:35:07  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public interface IBaseAreaAdminRSV {
    
    /**
     * 
     * fetchChildAreaAdmin:<br/>
     * 根据 请求参数中的 parentAreaCode 获取 该区域下的子节点信息； 
     * 
     * @param dto
     * @return 
     * @since JDK 1.6
     */
    public List<BaseAreaAdminRespDTO> fetchChildAreaAdmin(BaseAreaAdminReqDTO dto);
    
    /**
     * 
     * fetchAreaAdmin: <br/> 
     * 根据请求参数中的 areaCode 获取该区域信息；
     * 
     * @param dto
     * @return 
     * @since JDK 1.6
     */
    public BaseAreaAdminRespDTO fetchAreaAdmin(BaseAreaAdminReqDTO dto);
}

