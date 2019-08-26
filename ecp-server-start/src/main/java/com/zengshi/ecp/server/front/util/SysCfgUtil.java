/** 
 * File Name:SysCfgUtil.java 
 * Date:2015-8-18 15:44:27 
 * 
 */ 
package com.zengshi.ecp.server.front.util;

import java.util.List;

import javax.annotation.Resource;

import com.zengshi.ecp.server.front.dto.BaseSysCfgRespDTO;
import com.zengshi.ecp.server.front.param.IBaseSysCfgRSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.zengshi.ecp.server.front.exception.BusinessException;
import com.zengshi.paas.utils.CacheUtil;
import com.zengshi.paas.utils.LogUtil;
import com.zengshi.paas.utils.StringUtil;

/**
 * Project Name:ecp-services-sys <br>
 * Description: 与系统配置参数 缓存相关的内容<br>
 * Date:2015-8-18 15:44:27  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
//@Service
public class SysCfgUtil {
    
    private static final String MODULE = SysCfgUtil.class.getName();
    
    private static final String CACHE_KEY = "ECP.SYS.SYSCFG.Map";
    
    private static IBaseSysCfgRSV baseSysCfgRSV;

//    @Resource(name="baseSysCfgRSV")
    @Autowired(required = false)
    @Qualifier("baseSysCfgRSV")
    public void setBaseSysCfgRSV(IBaseSysCfgRSV baseSysCfgRSV) {
        SysCfgUtil.baseSysCfgRSV = baseSysCfgRSV;
    }
    
    /**
     * 
     * clearCache: 清理系统缓存 <br/> 
     * 
     * @return 
     * @since JDK 1.6
     */
    public static long clearCache(){
        
        LogUtil.info(MODULE, "========开始清理系统参数缓存；");
        CacheUtil.delItem(CACHE_KEY);
        LogUtil.info(MODULE, "========清理系统参数缓存结束；");
        
        return 1L;
    }

    /**
     * 
     * fetchSysCfg:获取系统配置参数 <br/> 
     *  从缓存中获取，如果缓存中没有，那么调用Dubbo服务，获取之后，再写入缓存；
     * @param paraCode
     * @return 
     * @since JDK 1.6
     */
    public static BaseSysCfgRespDTO fetchSysCfg(String paraCode){
        Object o = CacheUtil.hgetItem(CACHE_KEY, paraCode);
        if(o == null){
            LogUtil.info(MODULE, "根据系统参数编码："+paraCode+"从缓存中未获取到信息");
            BaseSysCfgRespDTO dto = baseSysCfgRSV.queryCfgByCode(paraCode);
            if(dto == null){
                LogUtil.error(MODULE, "根据系统参数编码："+paraCode+"从数据库中获取到的信息为空");
                throw new BusinessException(EcpSysCodeConstants.SYS_SYSCFG_NOEXISTS,new String[]{paraCode});
            }
            CacheUtil.hsetItem(CACHE_KEY, paraCode, dto);
            return dto;
        }
        return (BaseSysCfgRespDTO)o;
    }
    
    /**
     * 
     * checkSysCfgValue: 检查系统配置的参数值，与待检查值是否一致<br/> 
     * 如果待检测值为空（null, “”），那么参数值获取为空，即返回true;
     * 如果待检测值非空，那么获取的参数值要与待检测值一致，（equalIgnoreCase），返回true;
     * 以上两个都不满足，则返回false；
     * 如果根据系统参数编码（入参 paraCode)，获取不到参数信息，则抛出异常；
     * 
     * @param paraCode
     * @param needCheckValue
     * @return 
     * @since JDK 1.6
     */
    public static boolean checkSysCfgValue(String paraCode, String needCheckValue){
        BaseSysCfgRespDTO dto = fetchSysCfg(paraCode);
        if(dto == null){
            LogUtil.error(MODULE, "根据系统参数编码："+paraCode+"未获取到信息");
            throw new BusinessException(EcpSysCodeConstants.SYS_SYSCFG_NOEXISTS,new String[]{paraCode});
        }
        
        if(StringUtil.isEmpty(needCheckValue)){
          //如果待检查的值为空，那么只需要判断 dto.getParaValue是否为空即可
            return StringUtil.isEmpty(dto.getParaValue());
        } else {
            return needCheckValue.equalsIgnoreCase(dto.getParaValue());
        }
    }
    /**
     * 
     * delSysCfg:删除系统配置参数 <br/> 
     * @param paraCode
     * @return 
     * @since JDK 1.7
     */
    public static boolean delSysCfg(String paraCode){
        
        Object o = CacheUtil.hgetItem(CACHE_KEY, paraCode);
        if(o!=null){
            CacheUtil.hdelItem(CACHE_KEY, paraCode);
            return true;
        }
        return false;
    }
    
    /**
     * 
     * refreshSysCfgByCode:根据编码，从数据库中重置该参数； <br/> 
     * 
     * @param paraCode
     * @return 
     * @since JDK 1.6
     */
    public static BaseSysCfgRespDTO refreshSysCfgByCode(String paraCode){
        
        BaseSysCfgRespDTO dto = baseSysCfgRSV.queryCfgByCode(paraCode);
        if(dto == null){
            LogUtil.error(MODULE, "根据系统参数编码："+paraCode+"从数据库中获取到的信息为空");
            throw new BusinessException(EcpSysCodeConstants.SYS_SYSCFG_NOEXISTS,new String[]{paraCode});
        }
        CacheUtil.hsetItem(CACHE_KEY, paraCode, dto);
        return dto;
    }
}

