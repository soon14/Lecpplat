/**
 * SrpAccessDecisionManager.java	  V1.0   2013-2-26 下午5:31:46
 *
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.zengshi.ecp.base.security;

import com.zengshi.ecp.base.util.ParamConstant;
import com.zengshi.paas.utils.ResourceMsgUtil;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Iterator;

/**
 * 
 * 功能描述：用户拥有的权限 
 *
 *
 * 修改历史：(修改人，修改时间，修改原因/内容)
 */
public class EcpAccessDecisionManager implements AccessDecisionManager {

    @Override
    public void decide(Authentication authentication, Object object,
            Collection<ConfigAttribute> configAttributes) throws AccessDeniedException,
            InsufficientAuthenticationException {

        if(configAttributes == null){
            return ;
        }
        Iterator<ConfigAttribute> ite=configAttributes.iterator();
        while(ite.hasNext()){
            ConfigAttribute ca=ite.next();
            String needRole=((SecurityConfig)ca).getAttribute();
            for(GrantedAuthority ga:authentication.getAuthorities()){
                //如果权限匹配或是超级权限
                if(needRole.equals(ga.getAuthority()) || ParamConstant.AUTHORITY_ROLE_SUPER.equals(ga.getAuthority())){  
                    return;
                }
            }
        }
        throw new AccessDeniedException(ResourceMsgUtil.getMessage("webcore.000011", new Object[]{}));
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {

        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {

        return true;
    }

}
