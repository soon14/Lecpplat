/** 
 * Date:2015-8-29下午5:20:56 
 * 
 */ 
package com.zengshi.ecp.base.controller;

import com.zengshi.ecp.server.front.dto.BaseAreaAdminRespDTO;
import com.zengshi.ecp.server.front.util.BaseAreaAdminUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: <br>
 * Date:2015-8-29下午5:20:56  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
@Controller
@RequestMapping(value="/area")
public class EcpAreaController extends EcpBaseController{
    
    /**
     * 获取 当前区域编码的下级区域类表信息
     * @param areaCode ; 当前编码；
     * @return
     */
    @RequestMapping(value = "/fetchChilds")
    @ResponseBody
    public List<BaseAreaAdminRespDTO> fetchChilds(String areaCode){
        List<BaseAreaAdminRespDTO> lst = BaseAreaAdminUtil.fetchChildAreaInfos(areaCode);
        if(lst == null){
            //修改为一个空的串； 而不是直接返回 null;
            //为了避免外部不对该返回值进行封装；
            return new ArrayList<BaseAreaAdminRespDTO>();
        } else {
            return lst;
        }
    }
    
}

