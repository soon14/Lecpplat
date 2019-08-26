/**
 * Date:2015年9月2日下午3:44:19 
 *
 */
package com.zengshi.ecp.base.security;

import com.zengshi.ecp.base.util.SecurityLocaleUtil;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import com.zengshi.ecp.server.front.util.PasswordUtils;
import com.zengshi.paas.utils.LogUtil;
import com.zengshi.paas.utils.Md5Util;

/**
 * Description: <br>
 * Date:2015年9月2日下午3:44:19  <br>
 *
 * @since JDK 1.6
 * @see
 */
@SuppressWarnings("deprecation")
public class ECPPasswordEncoder implements PasswordEncoder {

    private static final String MODULE = ECPPasswordEncoder.class.getName();

    private String encryAlgorithm;

    public ECPPasswordEncoder(String encryAlgorithm) {

        this.encryAlgorithm=encryAlgorithm;
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

        return passwordUtils.checkPassword(rawPass,encPass);
    }


    public static void main(String[] args) {
        System.out.println(Md5Util.encode("ECPSSE123456"));
    }
}
