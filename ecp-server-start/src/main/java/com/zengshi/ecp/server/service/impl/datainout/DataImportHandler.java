/** 
 * File Name:DataInoutHandler.java 
 * Date:2015年8月25日下午6:07:48 
 * 
*/
package com.zengshi.ecp.server.service.impl.datainout;

import java.io.InputStream;
import java.util.List;

/** 
 * Project Name:ecp-server-start <br>
 * Description: 数据导入处理类<br>
 * Date:2015年8月25日下午6:07:48  <br>
 * 
 * @since JDK 1.6 
 * @see       
 */
public abstract class DataImportHandler {

    private InputStream inputStream;
    private String fileName;
    private String fileType;
    private String moduleName;
    private String operator;
    
    /**
     * 
     * Creates a new instance of DataImportHandler. 
     * 
     * @param inputStream Excel文件流
     * @param fileName 文件名
     * @param fileType 文件类型  xls或xlsx
     * @param moduleName 模块名
     * @param operator 操作人
     */
    public DataImportHandler(InputStream inputStream,String fileName,String fileType,String moduleName,String operator){
        
        this.inputStream=inputStream;
        this.fileName=fileName;
        this.fileType=fileType;
        this.moduleName=moduleName;
        this.operator=operator;
    }
    /**
     * 
     * doCallback:数据处理回调方法. <br/> 
     * 
     * @param datas Excel文件数据
     * @param fileId mongodb文件存储ID
     * @return 
     * @since JDK 1.6
     */
    public abstract boolean doCallback(List<List<Object>> datas,String fileId);

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getOperator() {
        return operator;
    }
    
    
}

