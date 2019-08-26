/** 
 * File Name:BaseParamUtil.java 
 * Date:2015-8-20上午9:39:30 
 * 
 */ 
package com.zengshi.ecp.server.front.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.zengshi.ecp.server.front.dto.BaseParamCfgReqDTO;
import com.zengshi.ecp.server.front.dto.BaseParamDTO;
import com.zengshi.ecp.server.front.param.IBaseParamCfgRSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.zengshi.ecp.server.front.exception.BusinessException;
import com.zengshi.paas.utils.CacheUtil;
import com.zengshi.paas.utils.LocaleUtil;
import com.zengshi.paas.utils.LogUtil;
import com.zengshi.paas.utils.StringUtil;
import com.alibaba.fastjson.JSON;

/**
 * Project Name:ecp-services-sys <br>
 * Description: <br>
 * Date:2015-8-20上午9:39:30  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
//@Service("baseParamUtil")
public class BaseParamUtil {
    private static final String MODULE = BaseParamUtil.class.getName();
    
    private static final String CACHE_KEY = "ECP.SYS.PARAM";
    
    private static IBaseParamCfgRSV baseParamCfgRSV;
    
//    @Resource(name="baseParamCfgRSV")
    @Autowired(required = false)
    @Qualifier("baseParamCfgRSV")
    public void setBaseParamCfgRSV(IBaseParamCfgRSV baseParamCfgRSV) {
        BaseParamUtil.baseParamCfgRSV = baseParamCfgRSV;
    }
    
    /**
     * 
     * clearCache:清理 业务参数缓存 <br/> 
     * 
     * @return 
     * @since JDK 1.6
     */
    public static long clearCache(){
        List<Object> lst= CacheUtil.keys(CACHE_KEY+"*");
        if(lst == null || lst.size() == 0){
            return 0;
        } else {
            LogUtil.info(MODULE, "========开始清理缓存；缓存数量："+lst.size());
            for(Object obj : lst){
                String key = (String)obj;
                LogUtil.info(MODULE, "======清理缓存："+key);
                CacheUtil.delItem(key);
            }
            LogUtil.info(MODULE, "========清理缓存结束；");
            return lst.size();
        }
    }
    
    /**
     * 
     * fetchParamList:根据参数Key，获取参数信息列表; 语言以：上下文环境中的语言信息为准； <br/> 
     * 
     * @param paramKey
     * @return
     * @throws BusinessException 
     * @since JDK 1.6
     */
    public static List<BaseParamDTO> fetchParamList(String paramKey) throws BusinessException{
        if(StringUtil.isEmpty(paramKey)){
            LogUtil.info(MODULE, "根据参数Key 获取 参数列表信息，入参为空");
            return null;
        }
        
        return fetchParamList(paramKey,LocaleUtil.getLocalString());
    }
    
    /**
     * 
     * fetchParamList: 根据参数Key，以及语言，获取参数信息列表；如果语言为空，则取上下文环境中的语言信息<br/> 
     * 
     * @param paramKey
     * @param spLang
     * @return
     * @throws BusinessException 
     * @since JDK 1.6
     */
    private static List<BaseParamDTO> fetchParamList(String paramKey, String spLang) throws BusinessException{
        if(StringUtil.isEmpty(paramKey)){
            LogUtil.info(MODULE, "根据参数Key 获取 参数列表信息，入参为空");
            return null;
        }
        if(StringUtil.isEmpty(spLang)){
            spLang = LocaleUtil.getLocalString();
            LogUtil.info(MODULE, "根据参数Key 获取 参数列表信息，语言编码为空，设置为默认语言："+spLang);
        }
        
        //正式的Cache的Key；
        String setKey = buildSetKey(paramKey, spLang);
        String mapKey = buildMapKey(paramKey, spLang);
        
        ///从缓存中获取数据，如果数据为空，则调整为从数据中获取；
        List<BaseParamDTO> lst = fetchItemFromCache(setKey);
        
        if(lst  == null || lst.size() == 0){
            ///没有数据，需要转入 DB查询获取之后，再进行处理；
            LogUtil.info(MODULE, "根据参数Key :"+paramKey + ";语言编码："+spLang+"从缓存获取的列表为空，转为从DB获取");
            lst = fetchParamListFromDB(paramKey,spLang);
            if(lst == null || lst.size() == 0){
                LogUtil.info(MODULE, "根据参数Key :"+paramKey + ";语言编码："+spLang+"从DB获取的数据为空");
                return null;
            }
            ///有数据，则删除原来的Key；之后，循环写入； ,写入两个，
            CacheUtil.delItem(setKey);
            for(int index = 0;index < lst.size(); index++){
                BaseParamDTO dto = lst.get(index);
                CacheUtil.zaddItem(setKey, JSON.toJSONString(dto), dto.getSpOrder());
                CacheUtil.hsetItem(mapKey, dto.getSpCode(), dto);
            }
            LogUtil.info(MODULE, "根据参数Key :"+paramKey + ";语言编码："+spLang+"从DB获取的数据有值，写入缓存，并输出");
        }
        
        return lst;
    }
    
    /**
     * 
     * fetchItemFromCache: 从缓存中获取数据 <br/> 
     * 
     * @param setKey
     * @return 
     * @since JDK 1.6
     */
    private static List<BaseParamDTO> fetchItemFromCache(String setKey){
        List<BaseParamDTO> dtos = new ArrayList<BaseParamDTO>();
        
        Set<String> sets = CacheUtil.zgetItems(setKey);
        if(sets == null || sets.size() == 0){
            return null;
        } else {
            for(String s : sets){
                BaseParamDTO dto = JSON.parseObject(s, BaseParamDTO.class);
                dtos.add(dto);
            }
            return dtos;
        }
    }
    
    /**
     * 
     * fetchParamValue: 根据参数Key， 编码，获取对应的参数值； <br/> 
     * 
     * @param paramKey
     * @param spCode
     * @return
     * @throws BusinessException 
     * @since JDK 1.6
     */
    public static String fetchParamValue(String paramKey, String spCode) throws BusinessException{
        return fetchParamValue(paramKey,spCode, LocaleUtil.getLocalString());
    }
    
    /**
     * 
     * fetchParamDTO: 根据参数Key， 编码，获取对应的参数信息； <br/> 
     * 
     * @param paramKey
     * @param spCode
     * @return
     * @throws BusinessException 
     * @since JDK 1.6
     */
    public static BaseParamDTO fetchParamDTO(String paramKey, String spCode) throws BusinessException{
        return fetchParamDTO(paramKey,spCode, LocaleUtil.getLocalString());
    }
    /**
     * 
     * fetchParamValue: 获取参数Key，参数编码 <br/> 
     * 
     * @param paramKey
     * @param spCode
     * @param spLang
     * @return
     * @throws BusinessException 
     * @since JDK 1.6
     */
    private static String fetchParamValue(String paramKey, String spCode, String spLang) throws BusinessException{
        BaseParamDTO dto = fetchParamDTO(paramKey,spCode,spLang);
        if(dto == null){
            return "";
        } else {
            return dto.getSpValue();
        }
    }
    
    /**
     * 
     * fetchParamDTO: 根据参数Key，参数编码，语言，获取参数信息；如果语言为空，那么参数取：spLang<br/> 
     * 
     * @param paramKey 参数Key
     * @param spCode  参数编码
     * @param spLang  语言
     * @return
     * @throws BusinessException 
     * @since JDK 1.6
     */
    private static BaseParamDTO fetchParamDTO(String paramKey, String spCode, String spLang) throws BusinessException{
        if(StringUtil.isEmpty(paramKey) || StringUtil.isEmpty(spCode)){
            LogUtil.info(MODULE, "根据参数Key 、参数编码 获取 参数信息，入参为空");
            return null;
        }
        if(StringUtil.isEmpty(spLang)){
            spLang = LocaleUtil.getLocalString();
            LogUtil.info(MODULE, "根据参数Key 获取 参数列表信息，语言编码为空，设置为默认语言："+spLang);
        }
        
        //正式的Cache的Key；
        String mapKey = buildMapKey(paramKey, spLang);
        
        Object o = CacheUtil.hgetItem(mapKey, spCode);
        if(o == null){
            LogUtil.info(MODULE, "根据参数Key :"+paramKey + ";语言编码："+spLang+"从缓存获取的列表为空，转为从DB获取");
            BaseParamDTO dto =  fetchParamFromDB(paramKey, spCode, spLang) ;
            if(dto == null){
                LogUtil.info(MODULE, "根据参数Key :"+paramKey + "; 参数编码:"+spCode+"; 语言编码:"+spLang+" 从DB获取的数据为空");
                return null;
            }
            ///有数据，则将现有的数据追加到List；并追加到Map一份；
            /**根据单个节点获取的数据，不能追加到List，由于获取列表的时候，是通过List获取的，如果这么处理的话，会导致从列表中返回有数据，但是数据不全；
             * String listKey = buildListKey(paramKey, spLang);
             * modify by yugn ; 2015.8.25
            ///CacheUtil.addItemToList(listKey, dto);*/
            CacheUtil.hsetItem(mapKey, spCode, dto);
            LogUtil.info(MODULE, "根据参数Key :"+paramKey + ";语言编码："+spLang+"从DB获取的数据有值，写入缓存，并输出");
            
            return dto;
        } else {
            
            BaseParamDTO dto = (BaseParamDTO)o;
            return dto;
        }

    }
    
    /**
     * 
     * fetchParamListFromDB:根据参数Key，以及语言编码，获取参数信息列表 <br/> 
     * 
     * @param paramKey
     * @param spLang
     * @return 
     * @since JDK 1.6
     */
    private static List<BaseParamDTO> fetchParamListFromDB(String paramKey, String spLang){
        
        BaseParamCfgReqDTO reqDTO = new BaseParamCfgReqDTO();
        
        reqDTO.setParamKey(paramKey);
        reqDTO.setSpLang(spLang);
        
        List<BaseParamDTO> lst = baseParamCfgRSV.fetchParamsByKey(reqDTO);
        
        return lst;
    }
    
    /**
     * 
     * fetchParamFromDB: 根据系统参数Key，参数编码，参数语言，获取参数信息；<br/> 
     * 
     * @param paramKey
     * @param spCode
     * @param spLang
     * @return 
     * @since JDK 1.6
     */
    private static BaseParamDTO fetchParamFromDB(String paramKey, String spCode, String spLang){
        BaseParamCfgReqDTO reqDTO = new BaseParamCfgReqDTO();
        reqDTO.setParamKey(paramKey);
        reqDTO.setSpCode(spCode);
        reqDTO.setSpLang(spLang);
        
        BaseParamDTO dto = baseParamCfgRSV.fetchParamByKeyAndCode(reqDTO);
        
        return dto;
    }
    
    
    /**
     * 
     * buildListKey: 创建List 的Key <br/> 
     * 
     * @param paramKey
     * @param spLang
     * @return 
     * @since JDK 1.6
     */
/*    private static String buildListKey(String paramKey, String spLang){
      //正式的Cache的Key；
        return CACHE_KEY + "." + paramKey + "." + spLang+".List";
    }*/
    
    /**
     * 
     * buildSetKey: 创建 Set 的Key <br/> 
     * 
     * @param paramKey
     * @param spLang
     * @return 
     * @since JDK 1.6
     */
    private static String buildSetKey(String paramKey, String spLang){
        //正式的Cache的Key；
          return CACHE_KEY + "." + paramKey + "." + spLang+".Set";
      }
    
    /**
     * 
     * buildListKey: 创建Map 的Key <br/> 
     * 
     * @param paramKey
     * @param spLang
     * @return 
     * @since JDK 1.6
     */
    private static String buildMapKey(String paramKey, String spLang){
        //正式的Cache的Key；
          return CACHE_KEY + "." + paramKey + "." + spLang+".Map";
      }
    
}

