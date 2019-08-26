/** 
 * File Name:BaseParamCfgResqDTO.java 
 * Date:2015-9-2下午2:44:54 
 * 
 */ 
package com.zengshi.ecp.server.front.dto;

import java.io.Serializable;

import com.zengshi.ecp.server.front.dto.BaseResponseDTO;

/**
 * Project Name:ecp-services-sys <br>
 * Description: <br>
 * Date:2015-9-2下午2:44:54  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class BaseParamCfgRespDTO extends BaseResponseDTO implements Serializable{

    /** 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = -3009986907307630050L;
    
    private String paramKey;

    private String paramName;
    
    private String paramLinkTable;

    private String paramLinkKey;

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamLinkTable() {
        return paramLinkTable;
    }

    public void setParamLinkTable(String paramLinkTable) {
        this.paramLinkTable = paramLinkTable;
    }

    public String getParamLinkKey() {
        return paramLinkKey;
    }

    public void setParamLinkKey(String paramLinkKey) {
        this.paramLinkKey = paramLinkKey;
    }
    
    

}

