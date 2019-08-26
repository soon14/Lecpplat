/** 
 * Date:2015-8-5下午5:05:53 
 * 
 */ 
package com.zengshi.ecp.base.controller;

import com.zengshi.ecp.base.vo.EcpBasePageRespVO;
import com.zengshi.butterfly.core.web.BaseController;
import org.springframework.ui.Model;

/**
 * Description: Ecp的所有Controller的父类<br>
 * Date:2015-8-5下午5:05:53  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class EcpBaseController extends BaseController {
    
    private final static String PAGE_VO_ATTR  = "gridResult";
    
    /**
     * 
     * addPageToModel: 分页查询结果<br/> 
     * 
     * @param model
     * @param vo 
     * @since JDK 1.6
     */
    public Model addPageToModel(Model model,EcpBasePageRespVO<?> vo){
        
         model.addAttribute(PAGE_VO_ATTR, vo);
         return model;
    }

}

