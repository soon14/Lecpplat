/**
 * SrpAuthenticationSuccessHandler.java	  V1.0   2013-2-28 上午10:12:40
 *
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.zengshi.ecp.base.security;

import com.zengshi.ecp.base.mvc.MessageModel;
import com.zengshi.ecp.base.mvc.ResponseModel;
import com.zengshi.ecp.base.mvc.ResponseModel.ResultTypeEnum;
import com.zengshi.ecp.base.security.handler.ILoginSuccessHandler;
import com.zengshi.ecp.base.util.ParamConstant;
import com.zengshi.ecp.base.util.WebContextUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


/**
 * 
 * 功能描述：自定义登录成功处理
 *
 *
 * 修改历史：(修改人，修改时间，修改原因/内容)
 */
public class EcpAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    
    private ILoginSuccessHandler successHandler;
    
    private String refererParameter;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
        
        if(null!=this.successHandler){
            this.successHandler.successHandle(request);
        }
        HttpSession session=request.getSession(false);
        if(null!=session){
            session.setAttribute(ParamConstant.LOGIN_FLAG_KEY,Boolean.TRUE);
        }
//        super.onAuthenticationSuccess(request, response, authentication);
//        request.setAttribute(ParamConstant.REQUEST_JSON_KEY, "{\"result\":\""+ParamConstant.JSON_AJAX_RESULT_TOKEN_RESULT+"\",\"message\":[],\"data\":{\"result\":\"S\"},\"token\":\"\"}");
        boolean isAjax = WebContextUtil.isAjaxRequest(request);
        if(isAjax){
            // ajax请求，返回json，替代redirect到login page
            if (logger.isDebugEnabled()) {
                logger.debug("ajax request or post");
            }
//            clearAuthenticationAttributes(request);
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            ResponseModel respModel=new ResponseModel();
            respModel.setAjaxResult(ResultTypeEnum.SUCCESS);
            respModel.setErrorMessage(new ArrayList<MessageModel>());
            JSONObject jsonObject = JSONObject.fromObject(respModel);
            String jsonString = jsonObject.toString();
            PrintWriter out= response.getWriter();
            out.print(jsonString);
            out.flush();
            out.close();
        }else{
            if(StringUtils.isNotBlank(refererParameter)){
                String refererUrl=request.getParameter(refererParameter.trim());
                if(StringUtils.isBlank(refererUrl)){
                    refererUrl=(String) request.getAttribute(refererParameter.trim());
                }
                if(StringUtils.isNotBlank(refererUrl)){
                    getRedirectStrategy().sendRedirect(request, response, refererUrl);
                }else{
                    super.onAuthenticationSuccess(request, response, authentication);
                }
            }else{
                super.onAuthenticationSuccess(request, response, authentication);
            }
            
        }
    }

    
    public void setSuccessHandler(ILoginSuccessHandler successHandler) {
    
        this.successHandler = successHandler;
    }

    public void setRefererParameter(String refererParameter) {
        this.refererParameter = refererParameter;
    }
    
    
}
