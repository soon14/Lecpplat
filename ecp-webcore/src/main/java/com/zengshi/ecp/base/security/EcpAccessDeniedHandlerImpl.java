/**
 * SrpAccessDeniedHandlerImpl.java	  V1.0   2013-2-28 下午2:11:19
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
import com.zengshi.ecp.base.util.ParamConstant;
import net.sf.json.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class EcpAccessDeniedHandlerImpl extends AccessDeniedHandlerImpl {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        boolean isAjax = ParamConstant.AJAX_REQUEST_HEAD_VALUE.equals(request.getHeader(ParamConstant.AJAX_REQUEST_HEAD_KEY));
        if(isAjax){
            // ajax请求，返回json，替代redirect到login page
            if (logger.isDebugEnabled()) {
                logger.debug("ajax request or post");
            }
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            ResponseModel respModel=new ResponseModel();
            respModel.setAjaxResult(ResultTypeEnum.ERROR);
            respModel.setErrorMessage(new ArrayList<MessageModel>());
            respModel.getErrorMessage().add(new MessageModel("",accessDeniedException.getMessage()));
            
            JSONObject jsonObject=JSONObject.fromObject(respModel);
            
            PrintWriter out= response.getWriter();
            out.print(jsonObject.toString());
            out.flush();
            out.close();
        }else{
            super.handle(request, response, accessDeniedException);
        }
    }
    
}
