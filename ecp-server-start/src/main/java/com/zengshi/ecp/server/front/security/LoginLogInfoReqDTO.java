package com.zengshi.ecp.server.front.security;

import com.zengshi.ecp.server.front.dto.BaseInfo;

import java.sql.Timestamp;

public class LoginLogInfoReqDTO extends BaseInfo{
    private String id;

    private Long staffId;

    private String staffLoginName;

    private Timestamp inTime;

    private Timestamp outTime;

    private String inIp;

    private String loginFrom;

    private String appKey;

    private String sessionId;

    private String remark;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getStaffLoginName() {
        return staffLoginName;
    }

    public void setStaffLoginName(String staffLoginName) {
        this.staffLoginName = staffLoginName == null ? null : staffLoginName.trim();
    }

    public Timestamp getInTime() {
        return inTime;
    }

    public void setInTime(Timestamp inTime) {
        this.inTime = inTime;
    }

    public Timestamp getOutTime() {
        return outTime;
    }

    public void setOutTime(Timestamp outTime) {
        this.outTime = outTime;
    }

    public String getInIp() {
        return inIp;
    }

    public void setInIp(String inIp) {
        this.inIp = inIp == null ? null : inIp.trim();
    }

    public String getLoginFrom() {
        return loginFrom;
    }

    public void setLoginFrom(String loginFrom) {
        this.loginFrom = loginFrom == null ? null : loginFrom.trim();
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey == null ? null : appKey.trim();
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId == null ? null : sessionId.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", staffId=").append(staffId);
        sb.append(", staffLoginName=").append(staffLoginName);
        sb.append(", inTime=").append(inTime);
        sb.append(", outTime=").append(outTime);
        sb.append(", inIp=").append(inIp);
        sb.append(", loginFrom=").append(loginFrom);
        sb.append(", appKey=").append(appKey);
        sb.append(", sessionId=").append(sessionId);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}

