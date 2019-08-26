package com.zengshi.ecp.server.front.security;

import com.zengshi.ecp.server.front.dto.BaseResponseDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class AuthPrivilegeResDTO extends BaseResponseDTO implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long id;

    private String roleAdmin;

    private String privilegeType;

    private String sysCode;

    private String status;

    private Long staffId; //用户ID

    private String staffCode;//登陆名

    private String staffClass;//用户类型

    private String password;//密码

    private String staffStatus;//账号状态

    private List<Long> privList ;//权限列表

    private String custLevelCode; //会员等级


    /**
     * 用户状态：
     *  4：冻结
     *  2：锁住
     *  3：失效
     * @return
     */
    public String getStaffStatus() {
        return staffStatus;
    }

    /**
     *  用户状态：
     * @param staffStatus 2：锁住  3：失效  4：冻结
     */
    public void setStaffStatus(String staffStatus) {
        this.staffStatus = staffStatus;
    }

    /**
     * 登录名
     * @return
     */
    public String getStaffCode() {
        return staffCode;
    }

    /**
     * 登录名
     * @param staffCode  用户名或编码
     */
    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    /**
     * 登录密码
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * 登录密码
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 权限集合
     * @return
     */
    public List<Long> getPrivList() {
        return privList;
    }

    /**
     * 权限集合
     * @param privList 权限id
     */
    public void setPrivList(List<Long> privList) {
        this.privList = privList==null?new ArrayList<Long>():privList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleAdmin() {
        return roleAdmin;
    }

    public void setRoleAdmin(String roleAdmin) {
        this.roleAdmin = roleAdmin;
    }

    public String getPrivilegeType() {
        return privilegeType;
    }

    public void setPrivilegeType(String privilegeType) {
        this.privilegeType = privilegeType;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getStaffClass() {
        return staffClass;
    }

    public void setStaffClass(String staffClass) {
        this.staffClass = staffClass;
    }

    public String getCustLevelCode() {
        return custLevelCode;
    }

    public void setCustLevelCode(String custLevelCode) {
        this.custLevelCode = custLevelCode;
    }
}
