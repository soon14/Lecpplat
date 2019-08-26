/**
 * SrpUserDetailsService.java	  V1.0   2013-2-22 下午4:00:12
 *
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.zengshi.ecp.base.security;

import com.zengshi.ecp.base.security.storage.SessionStorageFactory;
import com.zengshi.ecp.base.util.ParamConstant;
import com.zengshi.ecp.server.front.security.AuthPrivilegeResDTO;
import com.zengshi.ecp.server.front.security.IAuthRSV;
import com.zengshi.paas.utils.CacheUtil;
import com.zengshi.paas.utils.ResourceMsgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


/**
 * 
 * 功能描述：获取用户信息
 *
 *
 * 修改历史：(修改人，修改时间，修改原因/内容)
 */
public class EcpUserDetailsService implements UserDetailsService {

//    @Resource
    @Autowired(required = false)
    @Qualifier()
    private IAuthRSV authRSV;
    
    private String staffClass;
    
    
    public void setStaffClass(String staffClass) {
        this.staffClass = staffClass;
    }

    public void setAuthRSV(IAuthRSV authRSV) {
        this.authRSV = authRSV;
    }

    @Resource
    private SessionStorageFactory storageFactory;

    @Override
    public UserDetails loadUserByUsername(String username) {

        Collection<GrantedAuthority> auths = new HashSet<GrantedAuthority>();
        if(null==authRSV){
            return new EcpUserDetails(username,null,auths);
        }
        EcpUserDetails userDetails = null;
        AuthPrivilegeResDTO authPrivilege=this.authRSV.findPrivilByStaffCode(username,staffClass);
        if (null == authPrivilege) {
            throw new UsernameNotFoundException(ResourceMsgUtil.getMessage("webcore.000004", new Object[]{username}));
        }
        if("3".equals(authPrivilege.getStaffStatus())){
            throw new UsernameNotFoundException(ResourceMsgUtil.getMessage("webcore.000005", new Object[]{username}));
        }
//        if(!user.isAccountNonExpired()){
//            throw new UsernameNotFoundException(ResourceMsgUtil.getMessage("webcore.000006", new Object[]{username}));
//        }
        if("4".equals(authPrivilege.getStaffStatus())){
            throw new UsernameNotFoundException(ResourceMsgUtil.getMessage("webcore.000007", new Object[]{username}));
        }
        if("2".equals(authPrivilege.getStaffStatus())){
            throw new UsernameNotFoundException("您的账号出现异常，请与平台客服联系。<br />客服电话：400-817-6667");
        }
        List<Long> authIds=authPrivilege.getPrivList();
        if(null!=authIds){
            for(Long authId : authIds) {
                if(null==authId){
                    continue;
                }
                GrantedAuthority auth = new SimpleGrantedAuthority(ParamConstant.AUTHORITY_ROLE_PREFIX+authId.toString());
                auths.add(auth);
            }
        }

        auths.add(new SimpleGrantedAuthority(ParamConstant.AUTHORITY_ROLE_PUBLIC));
        userDetails = new EcpUserDetails(authPrivilege.getStaffCode(), authPrivilege.getPassword(), auths);
        userDetails.setAuthPrivilege(authPrivilege);
        //by zp 登录成功后同步一下锁定信息防止redis与数据库同步异常导致的登录失败
        //生效
        if("1".equals(authPrivilege.getStaffStatus())){
        	CacheUtil.delItem(ParamConstant.USER_LOCK+authPrivilege.getStaffId());
        }else{
        	CacheUtil.addItem(ParamConstant.USER_LOCK+authPrivilege.getStaffId(), true);
        }
        if(null!=this.storageFactory){
            Map<String,Object> affiliatedObject=this.storageFactory.store(authPrivilege);
            userDetails.setAffiliatedObject(affiliatedObject);
        }
        
        return userDetails;
    }

}
