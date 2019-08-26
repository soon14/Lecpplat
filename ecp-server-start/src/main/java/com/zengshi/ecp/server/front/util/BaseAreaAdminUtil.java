/** 
 * File Name:BaseAreaAdminUtil.java 
 * Date:2015-8-25上午10:48:13 
 * 
 */ 
package com.zengshi.ecp.server.front.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;

import com.zengshi.ecp.server.front.dto.BaseAreaAdminReqDTO;
import com.zengshi.ecp.server.front.dto.BaseAreaAdminRespDTO;
import com.zengshi.ecp.server.front.param.IBaseAreaAdminRSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.zengshi.paas.utils.CacheUtil;
import com.zengshi.paas.utils.LogUtil;
import com.zengshi.paas.utils.StringUtil;
import com.alibaba.fastjson.JSON;

/**
 * Project Name:ecp-services-sys <br>
 * Description: <br>
 * Date:2015-8-25上午10:48:13  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
//@Service("baseAreaAdminUtil")
public class BaseAreaAdminUtil {
    
    private static final String MODULE = BaseAreaAdminUtil.class.getName();
    
    private static final String CACHE_KEY = "ECP.SYS.AREAADMIN";
    
    private static final String CACHE_KEY_MAP =  CACHE_KEY +".Map";
    
    private static final String CACHE_KEY_SET =  "ECP.SYS.AREAADMIN.KEY.SET";
    
    private static IBaseAreaAdminRSV baseAreaAdminRSV;
    
    /**
     * 国家级的上级编码，在获取国家编码的时候，可以认为是获取该静态变量的下级节点编码；
     */
    private static final String COUNTRY_PARENT_CODE = "X";
    
//    @Resource(name="baseAreaAdminRSV")
    @Autowired(required = false)
    @Qualifier("baseAreaAdminRSV")
    public void setBaseAreaAdminRSV(IBaseAreaAdminRSV baseAreaAdminRSV) {
        BaseAreaAdminUtil.baseAreaAdminRSV = baseAreaAdminRSV;
    }
    
    /**
     * 
     * clearCache:清理区域信息缓存<br/> 
     * 
     * @return 
     * @since JDK 1.6
     */
    public static long clearCache(){
        List<Object> lst= CacheUtil.keys(CACHE_KEY+"*");
        if(lst == null || lst.size() == 0){
            return 0;
        } else {
            LogUtil.info(MODULE, "========开始清理区域信息缓存；缓存数量："+lst.size());
            for(Object obj : lst){
                String key = (String)obj;
                LogUtil.info(MODULE, "======清理区域信息缓存："+key);
                CacheUtil.delItem(key);
            }
            LogUtil.info(MODULE, "========清理区域信息缓存结束；");
            return lst.size();
        }
    }
    
    /**
     * 
     * fetchCountryInfos: 获取国家编码 <br/> 
     * 
     * @return 
     * @since JDK 1.6
     */
    public static List<BaseAreaAdminRespDTO> fetchCountryInfos(){
        return fetchChildAreaInfos(COUNTRY_PARENT_CODE);
    }
    
    /**
     * 
     * fetchChildAreaInfos: 根据 区域编码，获取该区域下一级区域的列表信息 <br/>
     * 1）从缓存中获取，如果缓存中没有，则从数据库中获取； 数据库获取的数据，写入两份数据：list保存、map 保存，同时要将对应的List的key ，写入set下；
     * modify 2015.9.3 写入list的时候，存在重复写入的问题，导致取出的数据重复。考虑修改为 zSet进行存储；
     * @param parentAreaCode
     * @return 
     * @since JDK 1.6
     */
    public static List<BaseAreaAdminRespDTO> fetchChildAreaInfos(String parentAreaCode){
        if(StringUtil.isEmpty(parentAreaCode)){
            LogUtil.error(MODULE, "根据区域编码获取下级区域信息，入参为空！");
            return null;
        }
        String setKey = buildSetKey(parentAreaCode);
        
        List<BaseAreaAdminRespDTO> lst = fetchChildAreaFromCache(setKey);
        if(lst == null || lst.isEmpty()){
            LogUtil.info(MODULE, "根据区域编码:"+parentAreaCode+"从缓存中获取下级区域信息 为空，将从数据库中获取！");
            //从数据库中获取数据；
            lst = fetchChildAreaInfosFromDB(parentAreaCode);
            if(lst == null || lst.isEmpty()){
                LogUtil.info(MODULE, "根据区域编码:"+parentAreaCode+"从数据库中获取下级区域信息 为空，请检查区域编码！");
                return null;
            }
            /**
             * 将数据加入缓存；数据加入两次。一次是调用 addItemToList ，一次是调用 hsetItem;
             * 需要逆序写入，是为了适配 addItemToList的方法；保证取出来的结果还是之前查询结果的顺序；
             * modify by yugn 2015.9.3 修改为 set 进行存储；
             */
            Set<String> areaSets = new TreeSet<String>();
            //for(int index = lst.size()-1; index > -1 ; index--){
            for(int index = 0; index <lst.size() ; index++){
                BaseAreaAdminRespDTO dto = lst.get(index);
                CacheUtil.hsetItem(CACHE_KEY_MAP, dto.getAreaCode(), dto);
                //areaSets.add(JSON.toJSONString(dto));
                CacheUtil.zaddItem(setKey, JSON.toJSONString(dto), dto.getAreaOrder());
            }
            //CacheUtil.addSet(setKey, areaSets);
            /// list 的 key 加入缓存。用于后续的删除使用；
            CacheUtil.saddItem(CACHE_KEY_SET, setKey);
        }
        return lst;
    }
    
    /**
     * 
     * fetchChildAreaFromCache: 从缓存中获取子节点信息<br/> 
     * 
     * @param setKey set的Key名称；
     * @return 
     * @since JDK 1.6
     */
    private static List<BaseAreaAdminRespDTO> fetchChildAreaFromCache(String setKey){
        
        List<BaseAreaAdminRespDTO> dtos = new ArrayList<BaseAreaAdminRespDTO>();
        
        Set<String> sets = CacheUtil.zgetItems(setKey);
        if(sets == null || sets.size() == 0){
            return null;
        } else {
            for(String s : sets){
                BaseAreaAdminRespDTO dto = JSON.parseObject(s, BaseAreaAdminRespDTO.class);
                dtos.add(dto);
            }
            return dtos;
        }
    }

    /**
     * 
     * fetchAreaInfo: 根据区域编码获取区域信息； <br/> 
     *   1）先从缓存中获取，如果缓存中的内容为空，那么从数据库中获取；
     *   2）从数据库中获取区域数据，而后 将区域数据写入 Map；以及 List；
     * @param areaCode 区域编码
     * @return 
     * @since JDK 1.6
     */
    public static BaseAreaAdminRespDTO fetchAreaInfo(String areaCode){
        if(StringUtil.isEmpty(areaCode)){
            LogUtil.error(MODULE, "根据区域编码获取区域信息，入参为空！");
            return null;
        }
        
        Object obj = CacheUtil.hgetItem(CACHE_KEY_MAP, areaCode);
        ///从缓存中获取数据； 如果缓存中获取的数据为空，那么从数据中获取，并将从数据库中获取的结果，写入缓存；
        if(obj == null){
            LogUtil.info(MODULE, "根据编码："+areaCode+"；从缓存中未获取到匹配的区域信息，调整为从数据库中");
            BaseAreaAdminRespDTO dto = fetchAreaInfoFromDB(areaCode);
            if(dto == null){
                LogUtil.info(MODULE, "根据编码："+areaCode+"；从数据库中未获取到匹配的区域信息，请检查");
                return null;
            }
            ///追加到缓存；
            CacheUtil.hsetItem(CACHE_KEY_MAP, dto.getAreaCode(), dto);
            LogUtil.info(MODULE, "根据编码："+areaCode+"；从数据库中获取到匹配的区域信息，写入缓存，并返回");
            return dto;
        } else {
            BaseAreaAdminRespDTO dto = (BaseAreaAdminRespDTO)obj;
            return dto;
        }
    }
    
    /**
     * 
     * fetchAreaName: 根据区域编码 获取 区域名称 <br/> 
     * 
     * @param areaCode 区域编码
     * @return 
     * @since JDK 1.6
     */
    public static String fetchAreaName(String areaCode){
        BaseAreaAdminRespDTO dto = fetchAreaInfo(areaCode);
        if(dto == null){
            return "";
        } else {
            return dto.getAreaName();
        }
    }
    
    
    /**
     * 
     * fetchAreaInfoFromDB: 调用服务，从数据库中获取区域数据 <br/> 
、     * 
     * @param areaCode
     * @return 
     * @since JDK 1.6
     */
    private static BaseAreaAdminRespDTO fetchAreaInfoFromDB(String areaCode){
        BaseAreaAdminReqDTO reqDto = new BaseAreaAdminReqDTO();
        reqDto.setAreaCode(areaCode);
        BaseAreaAdminRespDTO dto = baseAreaAdminRSV.fetchAreaAdmin(reqDto);
        return dto;
    }
    
    /**
     * 
     * fetchChildAreaInfosFromDB: 从数据库中获取区域信息 <br/> 
     * 
     * @param parentAreaCode
     * @return 
     * @since JDK 1.6
     */
    private static List<BaseAreaAdminRespDTO> fetchChildAreaInfosFromDB(String parentAreaCode){
        BaseAreaAdminReqDTO reqDto = new BaseAreaAdminReqDTO();
        reqDto.setParenAreaCode(parentAreaCode);
        List<BaseAreaAdminRespDTO> dtos = baseAreaAdminRSV.fetchChildAreaAdmin(reqDto);
        return dtos;
    }
    
    /**
     * 
     * clearCacheArea:清空所有的区域表的缓存数据<br/> 
     * 
     * @since JDK 1.6
     */
    
    public static void clearCacheArea(){
        String listKey = buildListKey("*");
        
        CacheUtil.delItem(CACHE_KEY_MAP);
        
        ///所有区域表的所有list结构；
        Set<String> keys = CacheUtil.getSet(CACHE_KEY_SET);
        if(keys == null || keys.size() == 0){
            return ;
        }
        for(String key:keys){
            if(StringUtil.isEmpty(key)){
                continue;
            }
            CacheUtil.delItem(key);
        }
    }
    
    private static String buildListKey(String areaCode) {
        return CACHE_KEY +"."+areaCode+".List";
    }
    
    private static String buildSetKey(String areaCode) {
        return CACHE_KEY +"."+areaCode+".Set";
    }
}

