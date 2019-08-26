/** 
 * Date:2015-9-25下午4:09:15 
 * 
 */ 
package com.zengshi.ecp.base.filter;

import javax.servlet.http.HttpServletRequest;

/**
 * Description: <br>
 * Date:2015-9-25下午4:09:15  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public interface ISiteHandler {
    
    /**
     * 
     * doHander: 站点信息的处理<br/> 
     * 
     * @return 
     * @since JDK 1.6
     */
    public Long doHandler(HttpServletRequest request);
}

