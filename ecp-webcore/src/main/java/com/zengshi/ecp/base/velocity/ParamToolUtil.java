/** 
 * Date:2015-8-28下午8:34:24 
 * 
 */ 
package com.zengshi.ecp.base.velocity;

import com.zengshi.ecp.server.front.dto.BaseParamDTO;
import com.zengshi.ecp.server.front.dto.BaseSysCfgRespDTO;
import com.zengshi.ecp.server.front.util.BaseAreaAdminUtil;
import com.zengshi.ecp.server.front.util.BaseParamUtil;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.zengshi.ecp.server.front.util.SysCfgUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Description: <br>
 * Date:2015-8-28下午8:34:24  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class ParamToolUtil {

    /**
     * 根据参数名称 将对应编码的参数，转换为中文含义；
     * 如果参数名称，或者编码为空，那么返回空值；
     * @param name
     * @param code
     * @return
     */
    public String translate(String name,String code){
        if(StringUtils.isEmpty(name) || StringUtils.isEmpty(code)){
            return "";
        }
        return BaseParamUtil.fetchParamValue(name, code);
    }
    
    /**
     * 根据参数名称 获取参数列表；key：编码； value : 中文含义；
     * 如果参数名称为空，那么返回空值；
     * @param name
     * @return
     */
    public Map<String,String> list(String name){
        if(StringUtils.isEmpty(name)){
            return null;
        }
        Map<String,String> map = new TreeMap<>();
        List<BaseParamDTO> dtos = BaseParamUtil.fetchParamList(name);
        if(dtos == null || dtos.size() == 0){
           //空，不处理；
        } else {
            for(BaseParamDTO dto : dtos){
                map.put(dto.getSpCode(), dto.getSpValue());
            }
        }
        return map;
    }

    /**
     *
     * getSysConfigCode:(根据系统参数编码，获取参数值). <br/>
     * @param code
     * @return
     */
    public String getSysConfigCode(String code){
        if(StringUtils.isEmpty(code)){
            return "";
        }
        BaseSysCfgRespDTO sysCfg=SysCfgUtil.fetchSysCfg(code);
        if(sysCfg !=null){
            return sysCfg.getParaValue();
        }
        return "";
    }


    /**
     * 
     * getParamDTOList:(根据参数名称，返回对应集合). <br/> 
     * @param name
     * @return 
     * @since JDK 1.7
     */
    public List<BaseParamDTO> getParamDTOList(String name){
        if(StringUtils.isEmpty(name)){
            return new ArrayList<BaseParamDTO>();
        }
        return BaseParamUtil.fetchParamList(name);
        
    }
    
    /**
     * 
     * getAreaName:(根据区域编码转成汉字). <br/> 
     * 
     * @param code
     * @return 
     * @since JDK 1.7
     */
    public String getAreaName(String code){
        if(StringUtils.isEmpty(code)){
            return "";
        }
       return BaseAreaAdminUtil.fetchAreaName(code);
    }
     
}

