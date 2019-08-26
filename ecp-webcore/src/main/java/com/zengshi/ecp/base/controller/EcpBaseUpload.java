/** 
 * Date:2015-8-5下午5:06:29 
 * 
 */ 
package com.zengshi.ecp.base.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.zengshi.ecp.base.mvc.JsonResultThreadLocal;
import com.zengshi.ecp.base.mvc.UploadResultModal;
import com.zengshi.paas.utils.FileUtil;
import com.zengshi.paas.utils.ImageUtil;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * 功能描述：公用文件上传处理
 *
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public abstract class EcpBaseUpload extends EcpBaseController {
    
    private static Logger logger = LoggerFactory.getLogger(EcpBaseUpload.class);
    /**
     * 
     * 功能描述：自定义上传校验
     *
     * <p>创建日期 ：2015年12月17日 上午10:23:18</p>
     *
     * @return 校验不通过则返回具体错误提示信息
     * 返回null则会被认为校验通过
     *
     * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     */
    protected abstract UploadResultModal customUploadHandle(MultipartFile file,HttpServletRequest request) throws Exception;
    /**
     * 
     * 功能描述：富文本编辑器上传校验扩展
     *
     * <p>创建日期 ：2015年12月22日 下午3:00:04</p>
     *
     * @param file
     * @param request
     * @return
     *
     * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     */
    protected abstract String customEditorUploadHandle(MultipartFile file,HttpServletRequest request) throws Exception;
    
    
    /**
     * 
     * 功能描述：执行文件上传
     *
     * <p>创建日期 ：2015年12月17日 下午2:21:51</p>
     *
     * @param file
     * @param request
     * @param response
     *
     * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     */
    protected String doUpload(MultipartFile file,HttpServletRequest request, HttpServletResponse response) throws Exception {
    	JsonResultThreadLocal.set(false);

    	//定义允许上传的文件扩展名
    	response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        //文件大小限制
        String fileSizeLimit = request.getParameter("fileSizeLimit");
        //文件类型限制
        String srcTypeExts = request.getParameter("fileTypeExts");
        //设置默认格式
        if(StringUtils.isEmpty(srcTypeExts))
        	srcTypeExts = "jpg,gif,bmp,png,pdf";
        String fileTypeExts = srcTypeExts;
        /*
        if(StringUtils.isNotEmpty(fileTypeExts)){
        	fileTypeExts = fileTypeExts.replaceAll("\\*\\.", "");
        }*/
        
        
    	//最大文件大小
    	long maxSize = this.getFileSize(fileSizeLimit);

    	if(!ServletFileUpload.isMultipartContent(request)) return getError("请选择文件。");
    	
    	if(file.getSize() > maxSize) return getError("上传文件大小超过限制或文件大小限制设置不正确。");
		//检查扩展名
		String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
		
		//如果是图片格式
		//且传递了图片宽度和图片高度的参数
		/*
		if(isImage(fileExt)){
			String imgHeight = request.getParameter("imgHeight");
			String imgWidth = request.getParameter("imgWidth");
			BufferedImage bi = ImageIO.read(file.getInputStream());
			Integer.parseInt(imgWidth)
		}
		*/
		
		if(!Arrays.<String>asList(fileTypeExts.split(",")).contains(fileExt)){
			return getError("上传文件扩展名是不允许的扩展名。\n只允许" + srcTypeExts + "格式。");
		}

		//自定义校验方法
		UploadResultModal checkResult = customUploadHandle(file, request);
		if(checkResult!=null && !checkResult.getSuccess()) return getError(checkResult);

		/*
		//文件上传到服务器本地目录的方式
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
		
		try{
			File uploadedFile = new File(savePath, newFileName);
			file.transferTo(uploadedFile);
		}catch(Exception e){
			return getError("上传文件失败。");
		}
		
    	FileItemFactory factory = new DiskFileItemFactory();
    	ServletFileUpload upload = new ServletFileUpload(factory);
    	upload.setHeaderEncoding("UTF-8");
    	List items = upload.parseRequest(request);
    	Iterator itr = items.iterator();
    	while (itr.hasNext()) {
    		FileItem item = (FileItem) itr.next();
    		String fileName = item.getName();
    		long fileSize = item.getSize();
    		if (!item.isFormField()) {
    			//检查文件大小
    			if(item.getSize() > maxSize){
    				model.addAttribute(getError("上传文件大小超过限制。"));
    				return model;
    			}
    			//检查扩展名
    			String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    			if(!Arrays.<String>asList(extMap.get(dirName).split(",")).contains(fileExt)){
    				model.addAttribute(getError("上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。"));
    				return model;
    			}

    			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
    			String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
    			try{
    				File uploadedFile = new File(savePath, newFileName);
    				item.write(uploadedFile);
    			}catch(Exception e){
    				model.addAttribute(getError("上传文件失败。"));
    				return model;
    			}

    			JSONObject obj = new JSONObject();
    			obj.put("error", 0);
    			obj.put("url", saveUrl + newFileName);
    			model.addAttribute(obj);
    		}
    	}
    	*/
		
		String fileId = FileUtil.saveFile(file.getBytes(), file.getOriginalFilename(), fileExt);
		//获得真实文件URL
		String url = "";
		if(this.isImage(fileExt)) url = ImageUtil.getImageUrl(fileId);
		else url = ImageUtil.getStaticDocUrl(fileId, fileExt) + "." + fileExt;
		
		logger.debug(fileId + "文件上传成功！");
		
		
		UploadResultModal result = new UploadResultModal();
		result.setSuccess(true);
		result.setFileId(fileId);
		result.setFileName(file.getOriginalFilename());
		result.setUrl(url);
		
		return JSONObject.toJSONString(result, true);
    }
    
    
    
    
    
    /**
     * 
     * 功能描述：Kindeditor文件上传处理
     *
     * <p>创建日期 ：2015-8-31 下午4:26:59</p>
     *
     * 表单POST参数
     * imgFile: 文件form名称
     * dir: 上传类型，分别为image、flash、media、file
     * 
     * 返回JSON字符串
     * 成功
     * {"error":0,"url":"xxx"}
     * 失败
     * {"error":1,"message":"错误信息"}
     *
     * @return
     *
     * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     */
    protected String doEditorFileUpload(MultipartFile file,HttpServletRequest request, HttpServletResponse response) throws Exception {
    	JsonResultThreadLocal.set(false);

    	//定义允许上传的文件扩展名
    	HashMap<String, String> extMap = new HashMap<String, String>();
    	extMap.put("image", "gif,jpg,jpeg,png,bmp");
    	extMap.put("flash", "swf,flv");
    	extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
    	extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2,pdf");
    	

    	//最大文件大小
    	long maxSize = 10000000;
    	response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

    	if(!ServletFileUpload.isMultipartContent(request)) return getEditorError("请选择文件。");

    	String dirName = request.getParameter("dir");
    	if (dirName == null) dirName = "image";
    	if(!extMap.containsKey(dirName)) return getEditorError("目录名不正确。");
    	
    	if(file.getSize() > maxSize) return getEditorError("上传文件大小超过限制。");
		//检查扩展名
		String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
		
		if(!Arrays.<String>asList(extMap.get(dirName).split(",")).contains(fileExt)){
			return getEditorError("上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。");
		}
		
		//自定义校验方法
		String customEditorUploadChecker = customEditorUploadHandle(file, request);
		if(customEditorUploadChecker != null) return getError(customEditorUploadChecker);

		/*
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
		try{
			File uploadedFile = new File(savePath, newFileName);
			file.transferTo(uploadedFile);
		}catch(Exception e){
			return getError("上传文件失败。");
		}
		*/
		//上传图片到图片服务器
		String fileId = FileUtil.saveFile(file.getBytes(), file.getOriginalFilename(), fileExt);
		//String fileId = ImageUtil.upLoadImage(file.getBytes(), file.getOriginalFilename());
		//获得真实文件URL
		String url = "";
		if(this.isImage(fileExt)) url = ImageUtil.getImageUrl(fileId);
		else url = ImageUtil.getStaticDocUrl(fileId, fileExt) + "." + fileExt;
		
		
		
		//保存文件，返回文件ID
		//FileUtil.saveFile(fileName, fileType);
		//保存图片，返回文件ID
		//ImageUtil.upLoadImage(image, name);
		//获得文件实际URL
		//ImageUtil.getImageUrl(imageId);
		
		logger.debug(fileId + "文件上传成功！");
		
		JSONObject obj = new JSONObject();
		obj.put("error", 0);
		obj.put("fileId", fileId);
		obj.put("url", url);
		//obj.put("fileId", "");
		//obj.put("url", "http://img1.mydrivers.com/img/20150904/189105bb132a40a9aebc2a3bf7096222.jpg");
    	return obj.toJSONString();
    }
    
    /**
     * 
     * 功能描述：文件删除服务，暂时为空实现
     *
     * <p>创建日期 ：2016年6月20日 下午4:48:57</p>
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     *
     * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     */
    protected String doDeleteUploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return "";
	}
    
    /**
     * 
     * 功能描述：处理错误信息的JSON结果
     *
     * <p>创建日期 ：2015-8-31 下午5:34:29</p>
     *
     * @param message
     * @return
     *
     * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     */
    /*
    protected String getError(String message) {
    	JSONObject obj = new JSONObject();
    	obj.put("error", 1);
    	obj.put("message", message);
    	obj.put("success", false);
    	return obj.toJSONString();
    }
    */
    
    /**
     * 
     * 功能描述：处理错误信息，返回信息模型
     *
     * <p>创建日期 ：2016年7月29日 下午4:53:15</p>
     *
     * @param message
     * @return
     *
     * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     */
    protected UploadResultModal getErrorModal(String message) {
    	UploadResultModal urm = new UploadResultModal();
    	urm.setSuccess(false);
    	urm.setError(message);
    	return urm;
    }
    
    /**
     * 
     * 功能描述：处理错误信息的JSON结果
     *
     * <p>创建日期 ：2015-8-31 下午5:34:29</p>
     *
     * @param message
     * @return
     *
     * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     */
    protected String getError(String message) {
    	return JSONObject.toJSONString(getErrorModal(message), true);
    }
    
    /**
     * 
     * 功能描述：处理错误信息的JSON结果
     *
     * <p>创建日期 ：2016年7月28日 上午11:15:53</p>
     *
     * @param urm
     * @return
     *
     * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     */
    protected String getError(UploadResultModal urm) {
    	return JSONObject.toJSONString(urm, true).toString();
    }
    
    /**
     * 
     * 功能描述：处理Kindeditor富文本文件上传失败的返回数据
     *
     * <p>创建日期 ：2016年7月28日 上午10:39:25</p>
     *
     * @param message
     * @return
     *
     * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     */
    protected String getEditorError(String message) {
    	JSONObject obj = new JSONObject();
    	obj.put("error", 1);
    	obj.put("message", message);
    	return obj.toJSONString();
    }
    
    /**
     * 
     * 功能描述：将XXXKB、XXXMB、XXXGB等值转换成具体的Bit值
     *
     * <p>创建日期 ：2015-11-20 下午5:33:17</p>
     *
     * @param fileSize
     * @return
     *
     * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     */
    protected long getFileSize(String fileSize){
        //默认设置为20M
    	if(StringUtils.isEmpty(fileSize)) return 20971520L;
    	long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
    	String tmpCode = fileSize.substring(fileSize.length()-2).toUpperCase();
    	String sizeNumber = fileSize.substring(0, fileSize.length()-2);
    	BigDecimal num = BigDecimal.valueOf(Long.parseLong(sizeNumber));
    	long result = 0L;
    	if(tmpCode.equals("KB")){
    		result = num.multiply(new BigDecimal(kb)).longValue();
    	}else if(tmpCode.equals("MB")){
    		result = num.multiply(new BigDecimal(mb)).longValue();
    	}else if(tmpCode.equals("GB")){
    		result = num.multiply(new BigDecimal(gb)).longValue();
    	}
    	return result;
    }
    
    /**
     * 
     * 功能描述：判断是否文件格式为图片
     *
     * <p>创建日期 ：2015年12月16日 下午4:55:52</p>
     *
     * @param type
     * @return
     *
     * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     */
    protected boolean isImage(String type){
    	if(StringUtils.isEmpty(type)) return false;
    	String imgType = "gif,jpg,jpeg,png,bmp";
    	if(imgType.indexOf(type.toLowerCase()) == -1) return false;
    	else return true;
    }
}

