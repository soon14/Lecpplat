/** 
 * File Name:BaseStaff.java 
 * Date:2015-8-18下午4:53:18 
 * 
 */ 
package com.zengshi.ecp.server.front.dto;

import java.io.Serializable;

/**
 * Project Name:ecp-server-start <br>
 * Description: 用户基础信息<br>
 * Date:2015-8-18下午4:53:18  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class BaseStaff implements Serializable{
    
    /** 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = -990423047502834466L;

    private long id;
    
    private String staffCode;
    
    private String staffClass;
    
    private String staffLevelCode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getStaffClass() {
        return staffClass;
    }

    public void setStaffClass(String staffClass) {
        this.staffClass = staffClass;
    }

	public String getStaffLevelCode() {
		return staffLevelCode;
	}

	public void setStaffLevelCode(String staffLevelCode) {
		this.staffLevelCode = staffLevelCode;
	}
    
    
}

