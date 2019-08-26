/** 
 * File Name:DataInoutUtil.java 
 * Date:2015年8月25日下午5:49:56 
 * 
*/
package com.zengshi.ecp.server.util;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.zengshi.ecp.server.dao.model.common.DataInout;
import com.zengshi.ecp.server.service.impl.datainout.DataImportHandler;
import com.zengshi.ecp.server.service.interfaces.datainout.IDataInoutSV;
import com.zengshi.paas.utils.ExcelFileUtil;

/** 
 * Project Name:ecp-server-start <br>
 * Description: 数据导入导出工具类<br>
 * Date:2015年8月25日下午5:49:56  <br>
 * 
 * @since JDK 1.6 
 * @see       
 */

@Component
public class DataInoutUtil {

    private static IDataInoutSV dataInoutSV;
    
    @Resource
    public void setIDataInoutSV(IDataInoutSV dataInoutSV){
        
        DataInoutUtil.dataInoutSV=dataInoutSV;
    }
    /**
     *
     * importExcel:导入Excel文件不保存原文件. <br/>
     *
     *
     * @param handler 数据处理类
     * @param startRow 开始读取数据行
     * @param startCell 开始读取数据列
     * @return  文件处理记录对象
     * @since JDK 1.6
     */
    public static DataInout importExcelWithoutSave(DataImportHandler handler,int startRow,int startCell){
        DataInout dataInout=new DataInout();
        dataInout.setBeginDate(new Timestamp(new Date().getTime()));
        Object[] object=ExcelFileUtil.importExcel(handler.getInputStream(), startRow, startCell, handler.getFileName(), handler.getFileType(),false);

        String fileId=(String)object[1];

        @SuppressWarnings("unchecked")
        boolean flag=handler.doCallback((List<List<Object>>)object[0], fileId);

        dataInout.setEndDate(new Timestamp(new Date().getTime()));
        dataInout.setFileId(fileId);
        dataInout.setFileName(handler.getFileName());
        dataInout.setModuleName(handler.getModuleName());
        dataInout.setOperator(handler.getOperator());
        dataInout.setOperateType((short)0);
        if(flag){
            dataInout.setStatus((short)1);
        }else{
            dataInout.setStatus((short)0);
        }
        dataInoutSV.save(dataInout);
        return dataInout;
    }
    /**
     * 
     * importExcel:导入Excel文件. <br/> 
     * 
     * 
     * @param handler 数据处理类
     * @param startRow 开始读取数据行
     * @param startCell 开始读取数据列
     * @return  文件处理记录对象
     * @since JDK 1.6
     */
    public static DataInout importExcel(DataImportHandler handler,int startRow,int startCell){
        DataInout dataInout=new DataInout();
        dataInout.setBeginDate(new Timestamp(new Date().getTime()));
        Object[] object=ExcelFileUtil.importExcel(handler.getInputStream(), startRow, startCell, handler.getFileName(), handler.getFileType());
        
        String fileId=(String)object[1];
        
        @SuppressWarnings("unchecked")
        boolean flag=handler.doCallback((List<List<Object>>)object[0], fileId);
        
        dataInout.setEndDate(new Timestamp(new Date().getTime()));
        dataInout.setFileId(fileId);
        dataInout.setFileName(handler.getFileName());
        dataInout.setModuleName(handler.getModuleName());
        dataInout.setOperator(handler.getOperator());
        dataInout.setOperateType((short)0);
        if(flag){
            dataInout.setStatus((short)1);
        }else{
            dataInout.setStatus((short)0);
        }
        dataInoutSV.save(dataInout);
        return dataInout;
    }
    
    /**
     * 
     * exportExcel:数据导出Excel. <br/> 
     * 
     * @param datas 数据集
     * @param columnNames 列名
     * @param fileName 文件名
     * @param fileType 文件类型： xls、xlsx
     * @param moduleName 模块名
     * @param operator 操作人
     * @return 
     * @since JDK 1.6
     */
    public static String exportExcel(List<List<Object>> datas,List<String> columnNames,String fileName,String fileType,
            String moduleName,String operator){
       DataInout dataInout=new DataInout();
       dataInout.setBeginDate(new Timestamp(new Date().getTime()));
       String fileId = ExcelFileUtil.exportExcel2Mongo(datas, columnNames, fileName, fileType);
       dataInout.setEndDate(new Timestamp(new Date().getTime()));
       dataInout.setFileId(fileId);
       dataInout.setFileName(fileName);
       dataInout.setModuleName(moduleName);
       dataInout.setOperator(operator);
       dataInout.setOperateType((short)1);
       if(null==fileId){
           dataInout.setStatus((short)0);
       }else{
           dataInout.setStatus((short)1);
       }
       
       dataInoutSV.save(dataInout);
       return fileId;
    }
}

