package com.zengshi.ecp.server.front.security;

import com.zengshi.ecp.server.front.dto.BaseResponseDTO;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class AuthMenuResDTO extends BaseResponseDTO implements Serializable {
    //AuthMenu
    private Long id;

    private String menuTitle;

    private String menuUrl;

    private Long parentMenuId;

    private String menuType;

    private String sysCode;

    private String menuDesc;

    private String menuPic;

    private Short sortOrder;

    private String status;

    private Long createStaff;

    private Timestamp createTime;

    private Long updateStaff;

    private Timestamp updateTime;
    
    private String menuCode;
    
    private Boolean isParent; //菜单树属性：是否父节点
    //AuthPrivilege
    private Long privilegeId;

    private String roleAdmin;

    private String privilegeType;

    private String privilegeSysCode;

    private String privilegeStatus;
    
    public List<AuthMenuResDTO> getSonList() {
        return sonList;
    }

    public void setSonList(List<AuthMenuResDTO> sonList) {
        this.sonList = sonList;
    }

    private List<AuthMenuResDTO> sonList;
    

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle == null ? null : menuTitle.trim();
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl == null ? null : menuUrl.trim();
    }


    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType == null ? null : menuType.trim();
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode == null ? null : sysCode.trim();
    }

    public String getMenuDesc() {
        return menuDesc;
    }

    public void setMenuDesc(String menuDesc) {
        this.menuDesc = menuDesc == null ? null : menuDesc.trim();
    }

    public String getMenuPic() {
        return menuPic;
    }

    public void setMenuPic(String menuPic) {
        this.menuPic = menuPic == null ? null : menuPic.trim();
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Long getCreateStaff() {
        return createStaff;
    }

    public void setCreateStaff(Long createStaff) {
        this.createStaff = createStaff;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateStaff() {
        return updateStaff;
    }

    public void setUpdateStaff(Long updateStaff) {
        this.updateStaff = updateStaff;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Long getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(Long privilegeId) {
        this.privilegeId = privilegeId;
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

    public String getPrivilegeSysCode() {
        return privilegeSysCode;
    }

    public void setPrivilegeSysCode(String privilegeSysCode) {
        this.privilegeSysCode = privilegeSysCode;
    }

    public String getPrivilegeStatus() {
        return privilegeStatus;
    }

    public void setPrivilegeStatus(String privilegeStatus) {
        this.privilegeStatus = privilegeStatus;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", menuTitle=").append(menuTitle);
        sb.append(", menuUrl=").append(menuUrl);
        sb.append(", parentMenuId=").append(parentMenuId);
        sb.append(", menuType=").append(menuType);
        sb.append(", sysCode=").append(sysCode);
        sb.append(", menuDesc=").append(menuDesc);
        sb.append(", menuPic=").append(menuPic);
        sb.append(", sortOrder=").append(sortOrder);
        sb.append(", status=").append(status);
        sb.append(", createStaff=").append(createStaff);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateStaff=").append(updateStaff);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }

    public Long getParentMenuId() {
        return parentMenuId;
    }

    public void setParentMenuId(Long parentMenuId) {
        this.parentMenuId = parentMenuId;
    }

    public Short getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Short sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(Boolean isParent) {
        this.isParent = isParent;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }
}
