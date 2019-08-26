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
public class BaseSysCfgRespDTO extends BaseResponseDTO implements Serializable {

    /** 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = -4316703319166271633L;

    private String paraCode;
    
    private String paraValue;
    
    private String paraDesc;

    public String getParaCode() {
        return paraCode;
    }

    public void setParaCode(String paraCode) {
        this.paraCode = paraCode;
    }

    public String getParaValue() {
        return paraValue;
    }

    public void setParaValue(String paraValue) {
        this.paraValue = paraValue;
    }

    public String getParaDesc() {
        return paraDesc;
    }

    public void setParaDesc(String paraDesc) {
        this.paraDesc = paraDesc;
    }

}

