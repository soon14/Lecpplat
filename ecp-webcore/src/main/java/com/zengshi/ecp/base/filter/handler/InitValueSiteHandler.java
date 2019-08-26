/** 
 * Date:2015-11-8下午5:02:22 
 * 
 */
package com.zengshi.ecp.base.filter.handler;

import com.zengshi.ecp.base.filter.ISiteHandler;
import com.zengshi.paas.utils.LogUtil;
import com.zengshi.butterfly.core.config.Application;
import com.zengshi.butterfly.core.web.WebConstants;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

/**
 * Description: <br>
 * Date:2015-11-8下午5:02:22 <br>
 * 
 * @version
 * @since JDK 1.6
 */
public class InitValueSiteHandler extends DefaultSiteHandler implements ISiteHandler {

    private static final String MODULE_NAME = InitValueSiteHandler.class.getName();

    @Override
    public Long doHandler(HttpServletRequest request) {

        Long t1 = Calendar.getInstance().getTime().getTime();
        LogUtil.debug(MODULE_NAME, "=====启动设定值获取站点编号方式；获取application-url中配置的参数值，如果未配置，返回默认参数");

        long siteId = this.fetchSiteByApplication();

        LogUtil.debug(MODULE_NAME, "=====结束设定值获取站点编号方式，耗时：" + (Calendar.getInstance().getTime().getTime() - t1) +" ms；站点ID："+siteId );
        
        return siteId;
    }

    private Long fetchSiteByApplication() {

        String siteId = Application.getValue(WebConstants.SITE_KEY);

        if (StringUtils.isEmpty(siteId)) {
            LogUtil.debug(MODULE_NAME, "=====启动设定值获取站点编号方式；application-url中未配置 site.key 参数作为站点ID，则返回默认参数");
            return fetchDefaultSite();
        } else {
            LogUtil.debug(MODULE_NAME, "=====启动设定值获取站点编号方式；application-url中配置的 site.key 为： "  + siteId + ";");
            try {
                return Long.parseLong(siteId);
            } catch (NumberFormatException err) {
                LogUtil.error(MODULE_NAME, "=====启动设定值获取站点编号方式；application-url中配置的 site.key 为： " + siteId + "，无法转为数字，将返回默认参数");
                return fetchDefaultSite();
            }
        }
    }
}
