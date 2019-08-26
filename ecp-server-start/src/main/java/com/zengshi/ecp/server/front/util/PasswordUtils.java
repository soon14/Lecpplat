/** 
 * File Name:PasswordUtils.java 
 * Date:2015-8-17上午10:24:18 
 * 
 */
package com.zengshi.ecp.server.front.util;

import com.zengshi.ecp.server.front.constans.ExptCodeConstants;
import com.zengshi.ecp.server.front.exception.BusinessException;
import com.zengshi.paas.utils.LogUtil;
import com.zengshi.paas.utils.Md5Util;
import com.zengshi.paas.utils.SHA1Util;
import com.zengshi.paas.utils.StringUtil;

/**
 * Project Name:ecp-server-start <br>
 * Description: 提供密码加密服务；根据选择的算法，返回加密后的密文，以及进行密文的校验 <br>
 * 支持：以下算法：<br>
 * MD5 ：对原始明文 采用MD5 算法加密；<br>
 * SHA1 ：对原始明文采用SHA1 算法加密；<br>
 * MD5-SALT ：对原始明文 采用MD5 加盐算法加密；<br>
 * SHA1-SALT：对原始明文 采用SHA-1 加盐算法加密；<br>
 * 建议采用后面两种加盐算法；<br>
 * 
 * Date:2015-8-17上午10:24:18 <br>
 * 
 * @version
 * @since JDK 1.6
 */
public class PasswordUtils {

    private final static String MODULE = PasswordUtils.class.getName();

    // 加盐算法的前缀
    private final static String PASSWORD_PREFIX = "ECPSSE";

    // 加密算法；目前支持的算法包括：
    ///MD5 、 SHA1 、 MD5-SALT 、SHA1-SALT
    private String encryAlgorithm;

    public String getEncryAlgorithm() {
        return encryAlgorithm;
    }

    public void setEncryAlgorithm(String encryAlgorithm) {
        this.encryAlgorithm = encryAlgorithm;
    }

    /**
     * 
     * encry: 对明文加密 <br/>
     * MD5 、 SHA1 是直接对入参进行对应的方式加密；
     * MD5-SALT \ SHA1-SALT 是对明文进行加盐之后，再调用对应的加密算法；
     * @param plaintext 需要加密的明文
     * @return
     * @exception 目前仅支持 MD5 、 SHA1 ； MD5-SALT、SHA1-SALT 四种加密方式；其它方式抛出异常；
     * @since JDK 1.6
     */
    public String encry(String plaintext) throws BusinessException{
        if(StringUtil.isEmpty(plaintext)){
            throw new BusinessException(ExptCodeConstants.Special.SYSTEM_ENCRY_NULL);
        }
        switch (encryAlgorithm) {
        case "MD5":
            return Md5Encry(plaintext);
          //  break;
        case "SHA1":
            return SHA1Encry(plaintext);
          //  break;
        case "MD5-SALT":
            return Md5Encry(addSalt(plaintext));
          //  break;
        case "SHA1-SALT":
            return SHA1Encry(addSalt(plaintext));
          //  break;
        default:
            throw new BusinessException(ExptCodeConstants.Special.SYSTEM_ENCRY_UNKNOWN);
        }
    }
    
    /**
     * 
     * checkPassword:校验密码是否一致； 
     * 
     * @param srcText 初始密码，未加密的明文；
     * @param passText 加密后的明文；
     * @return
     * @throws BusinessException 
     * @since JDK 1.6
     */
    public boolean checkPassword(String srcText,String passText) throws BusinessException{
        if(StringUtil.isEmpty(srcText) || StringUtil.isEmpty(passText)){
            throw new BusinessException(ExptCodeConstants.Special.SYSTEM_ENCRY_CHECK_NULL);
        }
        
        //对明文进行加密；
        String encryStr = this.encry(srcText);
        //返回 密文与入参密文的校验结果；
        return passText.equalsIgnoreCase(encryStr);
        
    }

    /**
     * addSalt:对字符串加盐<br/>
     * 
     * @param plaintext
     * @return
     * @since JDK 1.6
     */
    private String addSalt(String plaintext) {
        return PASSWORD_PREFIX + plaintext;
    }
    /**
     * 
     * Md5Encry: MD5加密方法<br/>
     * 
     * @param plaintext
     * @return
     * @since JDK 1.6
     */
    private String Md5Encry(String plaintext) {
        LogUtil.info(MODULE, "采用MD5进行加密处理");
        return Md5Util.encode(plaintext);
    }

    /**
     * 
     * SHA1Encry: SHA1 算法加密<br/>
     * 
     * @param plaintext
     * @return
     * @since JDK 1.6
     */
    private String SHA1Encry(String plaintext) {
        LogUtil.info(MODULE, "采用SHA1进行加密处理");
        return SHA1Util.encode(plaintext);
    }

}
