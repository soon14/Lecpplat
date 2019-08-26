/** 
 * Date:2015-8-10下午2:37:57 
 * 
 */ 
package com.zengshi.ecp.base.vo;

import java.io.Serializable;

/**
 * Description: <br>
 * Date:2015-8-10下午2:37:57  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class EcpBaseResponseVO implements Serializable {
    
    /** 
     * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
     * @since JDK 1.6 
     */ 
    private static final long serialVersionUID = 6405757007544350471L;

    public final static String RESULT_FLAG_SUCCESS = "ok";
    
    public final static String RESULT_FLAG_FAILURE = "fail";
    
    public final static String RESULT_FLAG_EXCEPTION = "expt";
    
    ///处理结果标志；
    private String resultFlag = RESULT_FLAG_SUCCESS;
    
    //处理结果信息；
    private String resultMsg;

    public String getResultFlag() {
        return resultFlag;
    }

    public void setResultFlag(String resultFlag) {
        this.resultFlag = resultFlag;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

}

