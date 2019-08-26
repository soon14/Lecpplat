/** 
 * File Name:BaseAreaAdminRespDTO.java 
 * Date:2015-8-24下午8:55:34 
 * 
 */ 
package com.zengshi.ecp.server.front.dto;

import com.zengshi.ecp.server.front.dto.BaseResponseDTO;

/**
 * Project Name:ecp-services-sys <br>
 * Description: 区域数据信息；<br>
 * Date:2015-8-24下午8:55:34  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class BaseAreaAdminRespDTO extends BaseResponseDTO {
    /** 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = -2747242814494301474L;

    /**
     * ---------------- ------------- -------- ------- -------------------------------- 
AREA_CODE        VARCHAR2(10)                   行政区划编码                     
AREA_NAME        VARCHAR2(128) Y                行政区划名称                     
PARENT_AREA_CODE VARCHAR2(10)  Y                上级编码                         
AREA_CODE_SHORT  VARCHAR2(16)  Y                编码简写                         
AREA_LEVEL       VARCHAR2(2)   Y                级别； 00 国家；10：省级；20 ： 地市；30：区县； 
AREA_ORDER       NUMBER        Y                排序                             
STATUS           VARCHAR2(1)   Y                状态；1：有效；0：无效           
CENTER_FLAG      VARCHAR2(2)   Y                中心区域标识；00：首都标识；01：省会标识；02：中心城区标识 
     */
    
    private String areaCode;
    
    private String areaName;
    
    private String parentAreaCode;
    
    private String areaCodeShort;
    
    private String areaLevel;
    
    private String centerFlag;
    
    private int areaOrder;

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getParentAreaCode() {
        return parentAreaCode;
    }

    public void setParentAreaCode(String parentAreaCode) {
        this.parentAreaCode = parentAreaCode;
    }

    public String getAreaCodeShort() {
        return areaCodeShort;
    }

    public void setAreaCodeShort(String areaCodeShort) {
        this.areaCodeShort = areaCodeShort;
    }

    public String getAreaLevel() {
        return areaLevel;
    }

    public void setAreaLevel(String areaLevel) {
        this.areaLevel = areaLevel;
    }

    public String getCenterFlag() {
        return centerFlag;
    }

    public void setCenterFlag(String centerFlag) {
        this.centerFlag = centerFlag;
    }

    public int getAreaOrder() {
        return areaOrder;
    }

    public void setAreaOrder(int areaOrder) {
        this.areaOrder = areaOrder;
    }
}

