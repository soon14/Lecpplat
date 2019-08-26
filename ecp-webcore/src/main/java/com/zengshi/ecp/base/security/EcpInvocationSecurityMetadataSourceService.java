/**
 * SrpInvocationSecurityMetadataSourceService.java	  V1.0   2013-2-26 下午5:21:13
 *
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.zengshi.ecp.base.security;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import java.util.Collection;


/**
 * 
 * 功能描述：资源与权限对应关系
 *
 *
 * 修改历史：(修改人，修改时间，修改原因/内容)
 */
public class EcpInvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {
    
    private ResourcesOfAuthentication resourcesOfAuthentication;
    
    public EcpInvocationSecurityMetadataSourceService(){
        
    }
    
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        
        return resourcesOfAuthentication.getAttributes(object);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {

        return resourcesOfAuthentication.getAllConfigAttributes();
    }

    @Override
    public boolean supports(Class<?> clazz) {

        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    
    public void setResourcesOfAuthentication(ResourcesOfAuthentication resourcesOfAuthentication) {
    
        this.resourcesOfAuthentication = resourcesOfAuthentication;
    }
    
}
