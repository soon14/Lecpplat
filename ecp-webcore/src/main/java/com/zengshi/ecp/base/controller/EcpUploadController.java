/** 
 * Date:2015-8-5下午5:06:29 
 * 
 */ 
package com.zengshi.ecp.base.controller;

import com.zengshi.ecp.base.mvc.JsonResultThreadLocal;
import com.zengshi.ecp.base.mvc.UploadResultModal;
import com.zengshi.ecp.base.mvc.annotation.NativeJson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * 功能描述：公用文件上传处理
 *
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
@Controller
@RequestMapping("/upload")
public class EcpUploadController extends EcpBaseUpload {
    
    private static Logger logger = LoggerFactory.getLogger(EcpIndexController.class);
    
    /**
     * 
     * 功能描述：公用上传文件页面
     *
     * <p>创建日期 ：2015-9-3 下午5:27:32</p>
     *
     * @return
     *
     * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     */
    @RequestMapping(value="/uploadSelector")
    public String uploadSelector(){
    	//return "/common/upload/common-upload";
    	return "/frameworkcomponent/upload/common-upload";
    }
    
    /**
     * 
     * 功能描述：文件上传处理
     *
     * <p>创建日期 ：2015-8-31 下午4:26:59</p>
     * 
     * 原使用controller进行json内容输出时，在ie8,9下，会弹出文件保存窗口，原因是输出的JSON内容，ie8,9会认为是输出为二进制文件进行下载
     * 为了兼容所有浏览器，需要将输出的内容头部修改为text/plain;charset=UTF-8
     *
     * @return
     *
     * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     */
    @RequestMapping(value="/publicFileUpload")
    public void fileUpload(@RequestParam(value = "uploadFileObj", required = false) MultipartFile file,Model model,HttpServletRequest request, HttpServletResponse response) throws Exception {
    	//JsonResultThreadLocal.set(false);
    	
    	String result = this.doUpload(file, request, response);
    	response.setCharacterEncoding("UTF-8"); 
    	response.setContentType("text/plain;charset=UTF-8");
    	
    	OutputStream out = response.getOutputStream();
    	out.write(result.getBytes("UTF-8"));
    	out.flush();
    	out.close();
    	//return "";
    }
    
    
    
    
    
    /**
     * 
     * 功能描述：文件上传处理
     *
     * <p>创建日期 ：2015-8-31 下午4:26:59</p>
     *
     * @return
     *
     * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     */
    @RequestMapping("/editorFileUpload")
    @ResponseBody
    @NativeJson(true)
    public String editorFileUpload(@RequestParam(value = "imgFile", required = false) MultipartFile file,Model model,HttpServletRequest request, HttpServletResponse response) throws Exception {
    	JsonResultThreadLocal.set(false);

    	String result = this.doEditorFileUpload(file, request, response);
    	return result;
    }
    
    /**
     * 
     * 功能描述：删除已上传的文件（暂时为空实现）
     *
     * <p>创建日期 ：2016年6月20日 上午11:42:21</p>
     *
     * @param model
     * @param request
     * @param response
     * @return
     * @throws Exception
     *
     * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     */
    @RequestMapping("/deleteUploadFile")
    @ResponseBody
    @NativeJson(true)
    public String deleteUploadFile(Model model,HttpServletRequest request, HttpServletResponse response) throws Exception {
    	JsonResultThreadLocal.set(false);
    	
    	String result = this.doDeleteUploadFile(request, response);
    	return null;
    }

    /**
     * 文件上传自定义校验
     */
	@Override
	protected UploadResultModal customUploadHandle(MultipartFile file, HttpServletRequest request) {
		return null;
	}

	/**
	 * 富文本编辑器文件上传自定义校验
	 */
	@Override
	protected String customEditorUploadHandle(MultipartFile file, HttpServletRequest request) {
		return null;
	}
    
}

