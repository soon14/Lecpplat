/**
 * UploadResultModal.java	  V1.0   2016年7月28日 上午9:51:24
 *
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.zengshi.ecp.base.mvc;

import java.io.Serializable;

/**
 * 功能描述：文件上传结果信息模型
 *
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class UploadResultModal implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8381488353935756957L;
	/**
	 * 成功为true，失败为false，该属性作为上传插件判断是否上传成功的状态属性
	 */
	private boolean success;
	/**
	 * 扩展信息属性，功能待定
	 */
	private String message;
	/**
	 * 当上传失败时，需要将错误信息设置到error中，上传插件会自动读取并展示内容
	 */
	private String error;
	/**
	 * 文件上传后的唯一标识ID，根据具体实现技术而定
	 */
	private String fileId;
	/**
	 * 文件完整名称，包含名称及后缀
	 */
	private String fileName;
	/**
	 * 文件完整访问URL
	 */
	private String url;
	
	
	
	
	
	
	
	
	
	public boolean getSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}	
}
