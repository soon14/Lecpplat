/** 
 * Date:2015-8-29下午1:18:19 
 * 
 */ 
package com.zengshi.ecp.base.velocity;

import com.zengshi.paas.utils.ImageUtil;
import com.alibaba.dubbo.common.utils.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Description: <br>
 * Date:2015-8-29下午1:18:19  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class AiToolUtil {
    
    /**
     * 验证target 包含source字符串
     * @param source
     * @param target
     * @return
     */
    public boolean containStr(String source,String target){
        if(StringUtils.isEmpty(source) || StringUtils.isEmpty(target)){
            return Boolean.FALSE;
        }
        return target.contains(source);
    }
    
    /**
     * 
     * genImageUrl: 根据图片ID ，规格，生成图片的URL<br/> 
     * 
     * @param id 图片保存的ID  如果id为空，则，使用默认图片；
     * @param size 图片的规格，高x宽  如果size为空，则 直接使用 id ;
     * @return 
     * @since JDK 1.6
     */
    public String genImageUrl(String id, String size,boolean isMark){
        String tmp = id;
        if(StringUtils.isEmpty(id)){
            tmp = ImageUtil.getDefaultImageId();
        }
        
        if(StringUtils.isEmpty(size)){
        } else {
            boolean flag=tmp.contains(".");
            String ntmp=tmp;
            if(flag){//jiangys 2017-02-21 主要是为了支持FastDFS的文件ID
                tmp=ntmp.substring(0,ntmp.lastIndexOf("."));
            }
            tmp = tmp+"_"+size;
            if(flag){
                tmp=tmp+ntmp.substring(ntmp.lastIndexOf("."));
            }
        }
        
        return ImageUtil.getImageUrl(tmp,isMark);
    }

    public String genImageUrl(String id, String size){

        return genImageUrl(id,size,false);
    }
    
    /**
     * 
     * escapeHtml: 将字符进行 html 的转义，防止内容中存在 <> 等字符，导致的问题；<br/> 
     * 
     * @param source
     * @return 
     * @since JDK 1.6
     */
    public String escapeHtml(String source){
        if(StringUtils.isEmpty(source)){
            return "";
        }
        return StringEscapeUtils.escapeHtml4(source);
    }

}

