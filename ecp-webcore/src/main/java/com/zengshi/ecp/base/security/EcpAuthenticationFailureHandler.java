/**
 * SrpAuthenticationFailureHandler.java	  V1.0   2013-2-28 上午10:35:12
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
import com.zengshi.ecp.base.security.handler.ILoginFailureHandler;
import com.zengshi.ecp.base.util.WebContextUtil;
import net.sf.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


/**
 * 
 * 功能描述：自定义登录失败处理
 *
 *
 * 修改历史：(修改人，修改时间，修改原因/内容)
 */
public class EcpAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    
    private ILoginFailureHandler failureHandler;
    
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        
        if(null!=this.failureHandler){
            this.failureHandler.failureHandle(request);
        }
        
        boolean isAjax = WebContextUtil.isAjaxRequest(request);
        if(isAjax){
            // ajax请求，返回json，替代redirect到login page
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            ResponseModel respModel=new ResponseModel();
            respModel.setAjaxResult(ResultTypeEnum.ERROR);
            respModel.setErrorMessage(new ArrayList<MessageModel>());
//            respModel.getErrorMessage().add(new MessageModel("webcore.000002",ResourceMsgUtil.getMessage("webcore.000002", new Object[]{})));
            respModel.getErrorMessage().add(new MessageModel("",exception.getMessage()));
            JSONObject jsonObject=JSONObject.fromObject(respModel);
            String jsonString=jsonObject.toString();
            PrintWriter out= response.getWriter();
            out.print(jsonString);
            out.flush();
            out.close();
        }else{
            super.onAuthenticationFailure(request, response, exception);
        }
        
    }

    
    public void setFailureHandler(ILoginFailureHandler failureHandler) {
    
        this.failureHandler = failureHandler;
    }

}
