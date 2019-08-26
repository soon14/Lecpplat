/** 
 * File Name:BaseParamCfgReqDTO.java 
 * Date:2015-8-20上午9:26:38 
 * 
 */ 
package com.zengshi.ecp.server.front.dto;

import com.zengshi.ecp.server.front.dto.BaseInfo;

/**
 * Project Name:ecp-services-sys <br>
 * Description: <br>
 * Date:2015-8-20上午9:26:38  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class BaseParamCfgReqDTO extends BaseInfo {

    /** 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = 8503246289759744658L;
    
    private String paramKey;
    
    private String spCode;
    
    private String spLang;

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getSpCode() {
        return spCode;
    }

    public void setSpCode(String spCode) {
        this.spCode = spCode;
    }

    public String getSpLang() {
        return spLang;
    }

    public void setSpLang(String spLang) {
        this.spLang = spLang;
    }
    
    

}

