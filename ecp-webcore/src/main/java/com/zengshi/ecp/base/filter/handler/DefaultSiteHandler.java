/** 
 * Date:2015-9-25下午4:15:02 
 * 
 */ 
package com.zengshi.ecp.base.filter.handler;

import com.zengshi.ecp.base.filter.ISiteHandler;
import com.zengshi.ecp.server.front.dto.BaseSysCfgRespDTO;
import com.zengshi.ecp.server.front.util.SysCfgUtil;
import com.zengshi.paas.utils.LogUtil;
import com.zengshi.paas.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * Description: <br>
 * Date:2015-9-25下午4:15:02  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class DefaultSiteHandler implements ISiteHandler {
    
    private static final String DEFAULT_SITE_KEY = "CMS_DEFAULT_SITE";
    
    private static final String MODULE_NAME = DefaultSiteHandler.class.getName();

    /** 
     * TODO 简单描述该方法的实现功能（可选）. 
     * @see com.zengshi.ecp.base.filter.ISiteHandler
     */
    @Override
    public Long doHandler(HttpServletRequest request) {
        LogUtil.debug(MODULE_NAME, "=====启动默认站点信息");
        return fetchDefaultSite();
    }
    
    /**
     * 
     * fetchDefaultSite: 获取默认站点<br/> 
     * 
     * @return 
     * @since JDK 1.6
     */
    protected Long fetchDefaultSite(){
        BaseSysCfgRespDTO dto = SysCfgUtil.fetchSysCfg(DEFAULT_SITE_KEY);
        if(dto == null || StringUtil.isEmpty(dto.getParaValue())){
            return 0L;
        } else {
            try{
               return Long.parseLong(dto.getParaValue());
            } catch(NumberFormatException err){
                return 0L;
            }
        }
    }

}

