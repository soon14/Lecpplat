/** 
 * File Name:BaseSysCfgDTO.java 
 * Date:2015-8-18����2:24:48 
 * 
 */ 
package com.zengshi.ecp.server.front.dto;

import java.io.Serializable;

import com.zengshi.ecp.server.front.dto.BaseResponseDTO;

/**
 * Project Name:ecp-services-sys <br>
 * Description: <br>
 * Date:2015-8-18 12:24:48  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class BaseParamDTO extends BaseResponseDTO implements Serializable {

    /** 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = -4316703319166271633L;

    private String spCode;
    
    private String spLang;
    
    private String spValue;
    
    private int spOrder;

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

    public String getSpValue() {
        return spValue;
    }

    public void setSpValue(String spValue) {
        this.spValue = spValue;
    }

    public int getSpOrder() {
        return spOrder;
    }

    public void setSpOrder(int spOrder) {
        this.spOrder = spOrder;
    }

    

}

