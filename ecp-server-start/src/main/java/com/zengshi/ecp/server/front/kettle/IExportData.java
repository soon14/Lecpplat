/** 
 * File Name:IExportData.java 
 * Date:2016年8月23日上午11:04:08 
 * 
*/
package com.zengshi.ecp.server.front.kettle;

import java.util.List;
import java.util.Map;

/** 
 * Project Name:pentaho-kettle <br>
 * Description: <br>
 * Date:2016年8月23日上午11:04:08  <br>
 * 
 * @since JDK 1.6 
 * @see       
 */
public interface IExportData {
    /**
     * 
     * export:输出数据. <br/> 
     * 
     * @param data
     * @return 
     * @since JDK 1.6
     */
    List<Map<String,Object>> export(Map<String, Object> data);
}

