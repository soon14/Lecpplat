/** 
 * File Name:IDataInoutSVImpl.java 
 * Date:2015年8月25日下午4:59:48 
 * 
*/
package com.zengshi.ecp.server.service.impl.datainout;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zengshi.ecp.frame.sequence.PaasSequence;
import com.zengshi.ecp.server.dao.mapper.common.DataInoutMapper;
import com.zengshi.ecp.server.dao.model.common.DataInout;
import com.zengshi.ecp.server.service.impl.GeneralSQLSVImpl;
import com.zengshi.ecp.server.service.interfaces.datainout.IDataInoutSV;

/** 
 * Project Name:ecp-server-start <br>
 * Description: <br>
 * Date:2015年8月25日下午4:59:48  <br>
 * 
 * @since JDK 1.6 
 * @see       
 */
@Service
public class DataInoutSVImpl extends GeneralSQLSVImpl implements IDataInoutSV {

    @Resource
    private DataInoutMapper mapper;
    
    @Resource(name="seq_base_data_inout_id")
    private PaasSequence seqence;
    
    @Override
    public void save(DataInout dataInout) {
        dataInout.setId(BigDecimal.valueOf(seqence.nextValue()));
        mapper.insert(dataInout);
    }

    @Override
    public void updateById(DataInout dataInout) {
        
        mapper.updateByPrimaryKeySelective(dataInout);
    }

    @Override
    public void deleteById(Long id) {
        
        mapper.deleteByPrimaryKey(BigDecimal.valueOf(id));
    }

    @Override
    public DataInout queryById(Long id) {
        
        return mapper.selectByPrimaryKey(BigDecimal.valueOf(id));
    }

}

