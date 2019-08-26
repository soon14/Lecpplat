/** 
 * File Name:BaseAreaAdminReqDTO.java 
 * Date:2015-8-24下午9:37:05 
 * 
 */ 
package com.zengshi.ecp.server.front.dto;

import com.zengshi.ecp.server.front.dto.BaseInfo;

/**
 * Project Name:ecp-services-sys <br>
 * Description: <br>
 * Date:2015-8-24下午9:37:05  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class BaseAreaAdminReqDTO extends BaseInfo{

    /** 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = -421486551581364236L;
    
    ///区域编码
    private String areaCode;
    
    //父节点编码
    private String parenAreaCode;

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getParenAreaCode() {
        return parenAreaCode;
    }

    public void setParenAreaCode(String parenAreaCode) {
        this.parenAreaCode = parenAreaCode;
    }
    
    
}

