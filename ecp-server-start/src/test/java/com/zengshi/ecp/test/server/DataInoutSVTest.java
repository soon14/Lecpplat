/** 
 * File Name:DataInoutSVTest.java 
 * Date:2015年8月25日下午5:10:31 
 * 
*/
package com.zengshi.ecp.test.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zengshi.ecp.server.dao.model.common.DataInout;
import com.zengshi.ecp.server.service.impl.datainout.DataImportHandler;
import com.zengshi.ecp.server.service.interfaces.datainout.IDataInoutSV;
import com.zengshi.ecp.server.util.DataInoutUtil;

/** 
 * Project Name:ecp-server-start <br>
 * Description: <br>
 * Date:2015年8月25日下午5:10:31  <br>
 * 
 * @since JDK 1.6 
 * @see       
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:ecp-service-context.xml")
public class DataInoutSVTest {

    @Resource
    private IDataInoutSV dataInoutSV;
    
    @Test
    @Ignore
    public void saveTest(){
        DataInout data=new DataInout();
        data.setBeginDate(new Timestamp(new Date().getTime()));
        data.setFileId("test");
        data.setModuleName("test");
        data.setOperator("tester");
        data.setStatus((short)1);
        data.setEndDate(new Timestamp(new Date().getTime()));
        
        dataInoutSV.save(data);
        
        DataInout nd=dataInoutSV.queryById(data.getId().longValue());
        
        Assert.assertEquals(1, nd.getId().intValue());
    }
    
    @Test
    @Ignore
    public void exportTest(){
        List<List<Object>> datas=new ArrayList<List<Object>>();
        
        List<String> titles=new ArrayList<String>();
        
        titles.add("序号");
        titles.add("名称");
        titles.add("日期");
        
        for(int i=0;i<20;i++){
            List<Object> data=new ArrayList<Object>();
            data.add(i);
            data.add("字符串"+i);
            data.add(new Date());
            datas.add(data);
        }
        
        String fileId=DataInoutUtil.exportExcel(datas, titles, "测试", "xlsx", "demo", "jys");
        System.out.println("++++++++++++++++++++++++++:"+fileId);
        
        Assert.assertNotNull(fileId);
    }
    
    @Test
    public void importTest(){
        FileInputStream fis=null;
        try {
            fis = new FileInputStream("G:\\121.xlsx");
            DataInoutUtil.importExcel(new DataImportHandler(fis,"测试文件","xlsx","demo","jys") {
                
                @Override
                public boolean doCallback(List<List<Object>> datas, String fileId) {
                    for(List<Object> row : datas){
                        for(Object cell : row){
                            System.out.println(cell);
                        }
                    }
                    System.out.println("****************************"+fileId);
                    
                    return true;
                }
            }, 2, 1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally{
            try {
                if(null!=fis){
                    fis.close();
                }
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }
}

