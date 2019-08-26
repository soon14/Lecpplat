/** 
 * Date:2015年9月2日下午3:44:19 
 * 
*/
package com.zengshi.ecp.base.security;

import com.zengshi.ecp.base.util.SecurityLocaleUtil;
import com.zengshi.ecp.server.front.util.PasswordUtils;
import com.zengshi.paas.utils.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.encoding.PasswordEncoder;

/** 
 * Description: <br>
 * Date:2015年9月2日下午3:44:19  <br>
 * 
 * @since JDK 1.6 
 * @see       
 */
@SuppressWarnings("deprecation")
public class ECPNullPasswordEncoder implements PasswordEncoder {
    
    private static final String MODULE = ECPNullPasswordEncoder.class.getName();
    
    private String encryAlgorithm;
    
    private String defaultPwd;
    
    public ECPNullPasswordEncoder(String encryAlgorithm) {
        
       this.encryAlgorithm=encryAlgorithm;
    }
    

    public void setDefaultPwd(String defaultPwd) {
        this.defaultPwd = defaultPwd;
    }



    @Override
    public String encodePassword(String rawPass, Object salt) {
        PasswordUtils passwordUtils=new PasswordUtils();
        passwordUtils.setEncryAlgorithm(encryAlgorithm);
        
        return passwordUtils.encry(rawPass);
    }

    @Override
    public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
    	 //如果指定了是跳过密码校验，那么此处直接返回true;
        if(SecurityLocaleUtil.isPasswordIgnore()){
            LogUtil.info(MODULE, "=========忽略了密码准确性的校验！==========");
            return true;
        }
        
        PasswordUtils passwordUtils=new PasswordUtils();
        passwordUtils.setEncryAlgorithm(encryAlgorithm);
        boolean flag= false ;
        /***
         * modify by yugn ;之前的方法，未捕获异常，在原始密码为空的时候，出现异常；
         */
        //先比对密码；
        try{
            flag = passwordUtils.checkPassword(rawPass,encPass);
        } catch(Exception err){
            //可能出现加密的异常；
            LogUtil.error(MODULE, "密码校验异常：rawPass: "+rawPass+"; encPass:"+encPass+"; " + "defaultPwd : "+defaultPwd+"; " + err.getMessage(), err);
        }
        //如果有设置初始密码，再与初始密码校验；
        if(StringUtils.isNotBlank(defaultPwd)){
            flag = flag || defaultPwd.equals(rawPass);
        }
        return flag;
    } 


}

