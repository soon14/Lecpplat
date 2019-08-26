/** 
 * File Name:MessageTest.java 
 * Date:2015-7-21下午7:19:59 
 * 
*/
package com.zengshi.ecp.test.server;

import java.util.Locale;

import com.zengshi.ecp.server.front.exception.BusinessException;
import com.zengshi.paas.utils.LocaleUtil;
import com.zengshi.paas.utils.ResourceMsgUtil;

/**
 * Project Name:ecp-server-start <br>
 * Description: <br>
 * Date:2015-7-21下午7:19:59  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class MessageTest {
    
    public static void main(String[] args){
        
        LocaleUtil.setLocale(Locale.JAPAN);
        
        System.out.println(ResourceMsgUtil.getMessage("demo.user", null));
        
        System.out.println(ResourceMsgUtil.getMessage("demo.pass", new Object[]{"ss"}));
        
        System.out.println(ResourceMsgUtil.getMessage("demo.msg2", null, null));
        System.out.println(ResourceMsgUtil.getMessage("demo.msg2.key", null, null));
        
        System.out.println(ResourceMsgUtil.getMessage("demo.user", null, Locale.JAPAN));
        
        System.out.println(ResourceMsgUtil.getMessage("demo.user", null, Locale.getDefault()));
        
        BusinessException err = new BusinessException("demo.user");
        System.out.println(err.getErrorCode());
        System.out.println(err.getErrorMessage());
    }
}

