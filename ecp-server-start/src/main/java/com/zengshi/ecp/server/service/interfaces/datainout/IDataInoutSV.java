/** 
 * File Name:IDataInoutSV.java 
 * Date:2015年8月25日下午4:58:34 
 * 
*/
package com.zengshi.ecp.server.service.interfaces.datainout;

import com.zengshi.ecp.server.dao.model.common.DataInout;
import com.zengshi.ecp.server.service.interfaces.IGeneralSQLSV;

/** 
 * Project Name:ecp-server-start <br>
 * Description: <br>
 * Date:2015年8月25日下午4:58:34  <br>
 * 
 * @since JDK 1.6 
 * @see       
 */
public interface IDataInoutSV extends IGeneralSQLSV {

    void save(DataInout dataInout);
    
    void updateById(DataInout dataInout);
    
    void deleteById(Long id);
    
    DataInout queryById(Long id);
}

