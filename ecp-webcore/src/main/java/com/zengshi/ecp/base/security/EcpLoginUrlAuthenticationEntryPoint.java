/**
 * SrpLoginUrlAuthenticationEntryPoint.java	  V1.0   2013-2-28 上午8:57:40
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
import com.zengshi.ecp.base.util.WebContextUtil;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


/**
 * 作用待确认，暂时没作用
 * 功能描述：区分ajax请求和非ajax请求的方式
 *
 *
 * 修改历史：(修改人，修改时间，修改原因/内容)
 */
public class EcpLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
    private static final Log logger = LogFactory.getLog(EcpLoginUrlAuthenticationEntryPoint.class);
    
    @Deprecated
    public EcpLoginUrlAuthenticationEntryPoint(){
        
    }
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        
        boolean isAjax = WebContextUtil.isAjaxRequest(request);
        if(isAjax){
            // ajax请求，返回json，替代redirect到login page
            if (logger.isDebugEnabled()) {
                logger.debug("ajax request or post");
            }
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            ResponseModel respModel=new ResponseModel();
            respModel.setAjaxResult(ResultTypeEnum.SUCCESS);
            respModel.setErrorMessage(new ArrayList<MessageModel>());
            JSONObject jsonObject=JSONObject.fromObject(respModel);
            
            String jsonString=jsonObject.toString();
            PrintWriter out= response.getWriter();
            out.print(jsonString);
            out.flush();
            out.close();
        }else{
            super.commence(request, response, authException);
        }
    }
    
    
}
