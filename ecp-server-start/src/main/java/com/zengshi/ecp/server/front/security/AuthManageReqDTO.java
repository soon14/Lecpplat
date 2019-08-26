package com.zengshi.ecp.server.front.security;

import com.zengshi.ecp.server.front.dto.BaseInfo;

import java.util.List;

public class AuthManageReqDTO extends BaseInfo {

    /** 
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
	 * @since JDK 1.7
	 */ 
	private static final long serialVersionUID = -3149218067992198533L;
	
	private List<String> sysCodes; //子系统
	private List<Long> privList; //权限集合
	
	
	public List<String> getSysCodes() {
		return sysCodes;
	}
	public void setSysCodes(List<String> sysCodes) {
		this.sysCodes = sysCodes;
	}
	public List<Long> getPrivList() {
		return privList;
	}
	public void setPrivList(List<Long> privList) {
		this.privList = privList;
	}
	
	

}

