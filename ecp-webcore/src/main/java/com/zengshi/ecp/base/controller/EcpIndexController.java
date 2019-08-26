/** 
 * Date:2015-8-5下午5:06:29 
 * 
 */ 
package com.zengshi.ecp.base.controller;

import com.zengshi.butterfly.core.config.Application;
import com.zengshi.butterfly.core.web.WebConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Description: <br>
 * Date:2015-8-5下午5:06:29  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
@Controller
@RequestMapping("/ecpindex")
public class EcpIndexController extends EcpBaseController {
    
    private static Logger logger = LoggerFactory.getLogger(EcpIndexController.class);
    
    /**
     * 
     * index: index页面的跳转 
     * 
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping()
    public String index(){
        String url = Application.getValue(WebConstants.URL_HOME_PAGE);
        logger.info("-----------home.page:------"+url);
        return "redirect:"+url;
    }
}

