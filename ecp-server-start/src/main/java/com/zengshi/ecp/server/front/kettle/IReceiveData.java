/** 
 * File Name:IReceiveData.java 
 * Date:2015年10月23日下午4:04:57 
 * 
*/
package com.zengshi.ecp.server.front.kettle;

import java.util.List;
import java.util.Map;

/** 
 * Project Name:pentaho-kettle <br>
 * Description: <br>
 * Date:2015年10月23日下午4:04:57  <br>
 * 
 * @since JDK 1.6 
 * @see       
 */
public interface IReceiveData {
    /**
     * 
     * receive:接收数据. <br/> 
     * 
     * @param data 
     * @since JDK 1.6
     */
    void receive(Map<String,Object> data);

}

