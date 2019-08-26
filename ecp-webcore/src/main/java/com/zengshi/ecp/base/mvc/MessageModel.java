/** 
 * Date:2015年8月11日下午4:01:22 
 * 
*/
package com.zengshi.ecp.base.mvc;

/** 
 * Description: 响应消息<br>
 * Date:2015年8月11日下午4:01:22  <br>
 * 
 * @since JDK 1.6 
 * @see       
 */
public class MessageModel {

    /**
     * 消息编码
     */
    private String code;
    /**
     * 消息体
     */
    private String message;
    
    public MessageModel(){
        
    }
    
    public MessageModel(String code,String message){
        this.code=code;
        this.message=message;
    }
    
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}

