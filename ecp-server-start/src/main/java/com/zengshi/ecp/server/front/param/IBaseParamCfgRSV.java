/** 
 * File Name:IBaseParamCfgRSV.java 
 * Date:2015-8-20上午9:09:35 
 * 
 */ 
package com.zengshi.ecp.server.front.param;

import java.util.List;

import com.zengshi.ecp.server.front.dto.BaseParamCfgReqDTO;
import com.zengshi.ecp.server.front.dto.BaseParamCfgRespDTO;
import com.zengshi.ecp.server.front.dto.BaseParamDTO;
import com.zengshi.ecp.server.front.exception.BusinessException;


/**
 * Project Name:ecp-services-sys <br>
 * Description: 与参数相关的服务<br>
 * Date:2015-8-20上午9:09:35  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public interface IBaseParamCfgRSV {
    
    /**
     * 
     * fetchParamsByKey: 根据参数Key，获取参数信息列表<br/> 
     * 
     * @param reqDTO  BaseParamCfgReqDTO ,其中的 paramKey 属性不能为空；
     * @return
     * @throws BusinessException 
     * @since JDK 1.6
     */
    public List<BaseParamDTO> fetchParamsByKey(BaseParamCfgReqDTO reqDTO) throws BusinessException;
    
    
    /**
     * 
     * fetchParamByKeyAndCode: 根据参数Key、参数编码，获取参数信息列表<br/> 
     * 
     * @param reqDTO  BaseParamCfgReqDTO ,其中的 paramKey、spCode 属性不能为空;
     *              如果 spLang 为空，那么取LocalUtil.getLocalString();
     * @return
     * @throws BusinessException 
     * @since JDK 1.6
     */
    public BaseParamDTO fetchParamByKeyAndCode(BaseParamCfgReqDTO reqDTO) throws BusinessException;
    
    
    /**
     * 
     * fetchAllParamConfig: 获取基础配置信息 <br/> 
     * 
     * @param reqDTO
     * @return
     * @throws BusinessException 
     * @since JDK 1.6
     */
    public List<BaseParamCfgRespDTO> fetchActiveParamConfig(BaseParamCfgReqDTO reqDTO) throws BusinessException;

}

