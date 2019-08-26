/**
 * ResourcesOfAuthentication.java	  V1.0   2013-5-31 上午10:18:37
 *
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.zengshi.ecp.base.security;

import com.zengshi.ecp.base.util.ParamConstant;
import com.zengshi.ecp.server.front.security.AuthManageReqDTO;
import com.zengshi.ecp.server.front.security.AuthMenuResDTO;
import com.zengshi.ecp.server.front.security.IMenuRSV;
import com.zengshi.paas.utils.CacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 
 * 功能描述：权限包含的资源类
 *
 *
 * 修改历史：(修改人，修改时间，修改原因/内容)
 */
public class ResourcesOfAuthentication {

//    @Resource
    @Autowired(required = false)
    @Qualifier("menuRSV")
    private IMenuRSV menuRSV;
    
    private List<String> sysCodes;
    
    @PostConstruct
    public Map<RequestMatcher, Collection<ConfigAttribute>> getAllResource(){
        
        Map<RequestMatcher, Collection<ConfigAttribute>> requestMap=new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
        List<AuthMenuResDTO> authReses= getAuthRes();
        for(AuthMenuResDTO authRes : authReses){
            Long privilegeId=authRes.getPrivilegeId();
            String auth=privilegeId!=null?(ParamConstant.AUTHORITY_ROLE_PREFIX+privilegeId.toString()):null;
//            if(authRes.getIsPublic()){
//                auth=ParamConstant.AUTHORITY_ROLE_PUBLIC;
//            }else if(!StringUtils.hasText(auth)){
//                auth=ParamConstant.AUTHORITY_ROLE_DEFAULT;
//            }
            if(!StringUtils.hasText(auth)){
                auth=ParamConstant.AUTHORITY_ROLE_DEFAULT;
            }
            ConfigAttribute ca = new SecurityConfig(auth);
            
            String url=authRes.getMenuUrl();
            if(!StringUtils.hasText(url)){
                continue;
            }
            if(!url.startsWith("/")){
                url="/"+url;
            }
            RequestMatcher matcher=new AntPathRequestMatcher(url);
            if(requestMap.containsKey(matcher)){
                requestMap.get(matcher).add(ca);
            }else{
                Collection<ConfigAttribute> atts = new HashSet<ConfigAttribute>();
                atts.add(ca);
                requestMap.put(matcher, atts);
            }
        }
        return requestMap;
    }
    
    @SuppressWarnings("unchecked")
    private List<AuthMenuResDTO> getAuthRes(){
        List<AuthMenuResDTO> authRes=new ArrayList<AuthMenuResDTO>();
        if(this.sysCodes!=null){
            for(String sysCode : sysCodes){
                List<AuthMenuResDTO> dto=(List<AuthMenuResDTO>) CacheUtil.getItem(sysCode);
                if(!CollectionUtils.isEmpty(dto)){
                    authRes.addAll(dto);
                }
            }
            if(CollectionUtils.isEmpty(authRes)){
                /**
                 * 此处做修改，要求传入的是 sysCodes 的后面一部分（sysCode: SYS_SUB_SYSTEM_2000，传入的是2000）
                 */
                List<String> codes = new ArrayList<String>();
                for(String sysCode : sysCodes){
                    String[] tmpCodes = sysCode.split("_");
                    codes.add(tmpCodes[tmpCodes.length-1]);
                }
                AuthManageReqDTO dto = new AuthManageReqDTO();
                dto.setSysCodes(codes);
                if(null!=menuRSV){
                    authRes=this.menuRSV.listAuthMenu(dto);
                }
            }
        }
        return authRes;
    }
    
    public Collection<ConfigAttribute> getAttributes(Object object){
        final HttpServletRequest request = ((FilterInvocation) object).getRequest();
        Map<RequestMatcher, Collection<ConfigAttribute>> requestMap=getAllResource();
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
            if (entry.getKey().matches(request)) {
                return entry.getValue();
            }
        }
        return null;
    }
    
    public Collection<ConfigAttribute> getAllConfigAttributes(){
        
        Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();
        Map<RequestMatcher, Collection<ConfigAttribute>> requestMap=getAllResource();
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
            allAttributes.addAll(entry.getValue());
        }

        return allAttributes;
    }

    public void setMenuRSV(IMenuRSV menuRSV) {
        this.menuRSV = menuRSV;
    }

    public void setSysCodes(List<String> sysCodes) {
        this.sysCodes = sysCodes;
    }

}
