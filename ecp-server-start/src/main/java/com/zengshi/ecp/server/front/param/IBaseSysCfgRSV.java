/** 
 * File Name:BaseSysCfgRSV.java 
 * Date:2015-8-18����4:12:51 
 * 
 */ 
package com.zengshi.ecp.server.front.param;

import com.zengshi.ecp.server.front.dto.BaseSysCfgReqDTO;
import com.zengshi.ecp.server.front.dto.BaseSysCfgRespDTO;
import com.zengshi.ecp.server.front.exception.BusinessException;

/**
 * Project Name:ecp-services-sys <br>
 * Description: <br>
 * Date:2015-8-18 14:12:51  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public interface IBaseSysCfgRSV {
    
    /**
     * 
     * saveCfg: 保存系统配置参数信息<br/> 
     *  
     * @param sysCfgDto 
     * @throws BusinessException 
     * @since JDK 1.6
     */
    public void saveCfg(BaseSysCfgReqDTO sysCfgDto) throws BusinessException;
    
    /**
     * 
     * queryCfgByCode: 根据系统参数编码获取系统参数配置信息 <br/> 
     * 
     * @param paramCode
     * @throws BusinessException 
     * @since JDK 1.6
     */
    public BaseSysCfgRespDTO queryCfgByCode(String paramCode) throws BusinessException;
}

