/** 
 * Date:2015年8月11日上午11:50:00 
 * 
*/
package com.zengshi.ecp.base.mvc;

import java.util.List;

/** 
 * Description: ajax请求返回对象<br>
 * Date:2015年8月11日上午11:50:00  <br>
 * 
 * @since JDK 1.6 
 * @see       
 */
public class ResponseModel {
    /***
     * 请求结果
     */
    private ResultTypeEnum ajaxResult= ResultTypeEnum.SUCCESS;
    /**
     * 异常消息
     */
    private List<MessageModel> errorMessage;
    /**
     * 实际返回对象
     */
    private Object values;
    
    
    public int getAjaxResult() {
        return ajaxResult.value;
    }

    public void setAjaxResult(ResultTypeEnum ajaxResult) {
        this.ajaxResult = ajaxResult;
    }

    public List<MessageModel> getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(List<MessageModel> errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Object getValues() {
        return values;
    }

    public void setValues(Object values) {
        this.values = values;
    }

    public enum ResultTypeEnum{
        //请求异常
        ERROR(0),
        //请求成功
        SUCCESS(1),
        //表单验证失败
        INVALID(2),
        //请求超时
        TIMEOUT(3);
        
        private int value;
        
        private ResultTypeEnum(int value){
            
            this.value=value;
        }
        
        public int getValue(){
            
            return this.value;
        }
    }

}
