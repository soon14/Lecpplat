/**
 * SrpUsernamePasswordAuthenticationFilter.java	  V1.0   2013-2-27 下午2:01:30
 *
 *
 * Modification history(By    Time    Reason):
 *
 * Description:
 */

package com.zengshi.ecp.base.security;

import com.zengshi.ecp.base.util.ParamConstant;
import com.zengshi.ecp.base.util.SecurityLocaleUtil;
import com.zengshi.ecp.server.front.exception.BusinessException;
import com.zengshi.ecp.server.front.security.IAuthRSV;
import com.zengshi.ecp.server.front.security.LoginPwdCntReqDTO;
import com.zengshi.paas.captcha.impl.CaptchaServlet;
import com.zengshi.paas.utils.LogUtil;
import com.zengshi.paas.utils.CacheUtil;
import com.zengshi.paas.utils.ResourceMsgUtil;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



/**
 *
 * 功能描述：自定义用户登录验证
 *
 *
 * 修改历史：(修改人，修改时间，修改原因/内容)
 */
public class EcpUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final static String MODULE = EcpUsernamePasswordAuthenticationFilter.class.getName();

	private final static Logger logger=Logger.getLogger(EcpUsernamePasswordAuthenticationFilter.class);

	private String checkCodeParameter=ParamConstant.LOGIN_CHECK_CODE;

	private boolean captchaCode=true;

	private boolean smsCode=true;


	private IAuthRSV authRSV;

	public void setAuthRSV(IAuthRSV authRSV) {
		this.authRSV = authRSV;
	}

	/**
	 *
	 * setCaptchaCode:是否验证码. <br/>
	 *
	 * @param captchaCode
	 * @since JDK 1.6
	 */
	public void setCaptchaCode(boolean captchaCode) {
		this.captchaCode = captchaCode;
	}
	/**
	 *
	 * setCaptchaCode:是否短信验证码. <br/>
	 *
	 * @param captchaCode
	 * @since JDK 1.6
	 */
	public void setSmsCode(boolean smsCode) {
		this.smsCode = smsCode;
	}

	protected void checkRequest(HttpServletRequest request){

		String username=obtainUsername(request);
		if(!StringUtils.hasText(username)){
			throw new AuthenticationServiceException(ResourceMsgUtil.getMessage("webcore.000015",new Object[]{}));
		}

		/**
		 * 如果忽略了密码校验，开启了短信验证则进行短信验证码非空校验
		 * 如果忽略了密码校验，开启了验证码校验则进行验证码非空验证
		 * 
		 */
		if(!SecurityLocaleUtil.isPasswordIgnore()){
			LogUtil.info(MODULE, "=========未忽略了密码准确性的校验！并且开启了密码、验证码非空验证==========");
			String password=obtainPassword(request);
			if(!StringUtils.hasText(password)){
				throw new AuthenticationServiceException(ResourceMsgUtil.getMessage("webcore.000016",new Object[]{}));
			}
			if(captchaCode){
				String captchaCode=obtainValidateCodeParameter(request);
				if(!StringUtils.hasText(captchaCode)){
					throw new AuthenticationServiceException(ResourceMsgUtil.getMessage("webcore.000014",new Object[]{}));
				}
			}
		}else{
			//如果忽略了密码校验，开启了短信验证则进行短信验证码非空校验
			if(smsCode){
				LogUtil.info(MODULE, "=========忽略了密码准确性的校验！并且开启了短信验证码验证==========");
				String SMScode=obtainSMScode(request);
				if(!StringUtils.hasText(SMScode)){
					throw new AuthenticationServiceException(ResourceMsgUtil.getMessage("webcore.000020",new Object[]{}));
				}
			}
			//如果忽略了密码校验，开启了验证码校验则进行验证码非空验证
			if(captchaCode){
				LogUtil.info(MODULE, "=========忽略了密码准确性的校验！并且开启了验证码非空验证==========");
				String captchaCode=obtainValidateCodeParameter(request);
				if(!StringUtils.hasText(captchaCode)){
					throw new AuthenticationServiceException(ResourceMsgUtil.getMessage("webcore.000014",new Object[]{}));
				}
			}
		}

	}

	/**
	 *
	 * doFromSSO: 根据请求来源，设置线程变量<br/>
	 *   如果请求是来自于SSO的，直接忽略密码的校验（同时还有验证码的校验）；
	 *	如果请求是来自于SMS的，直接忽略密码的校验（同时还有验证码的校验）；
	 * @param request
	 * @since JDK 1.6
	 */
	public void doFromSSO(HttpServletRequest request){
		String from = request.getParameter(ParamConstant.LOGIN_SSO_KEY);
		if(ParamConstant.LOGIN_SSO_VALUE.equalsIgnoreCase(from)){
			LogUtil.info(MODULE, "=========登录验证请求来源于 ："+from+"，后续处理中，将会忽略密码和验证码的准确性验证==========");
			SecurityLocaleUtil.setPasswordIgnore();
		}else if(ParamConstant.LOGIN_SMS_VALUE.equalsIgnoreCase(from)){
			LogUtil.info(MODULE, "=========登录验证请求来源于 ："+from+"，后续处理中，将会忽略密码的准确性验证==========");
			SecurityLocaleUtil.setPasswordIgnore();
		};
	}



	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		//由于tomcat采用的是线程池，会出现两个不同请求共用一个thread的情况，所以threadLocal 的生命周期不等于request的生命周期
		//这里要先清除当前线程原有的数据
		SecurityLocaleUtil.removePasswordIgnore();
		///先对来源做检查处理；
		doFromSSO(request);

		checkRequest(request);

		if(!SecurityLocaleUtil.isPasswordIgnore()){
			LogUtil.info(MODULE, "=========未忽略了密码准确性的校验！ 将进行验证码验证==========");
			// 是否开启登录验证
			if(captchaCode){
				LogUtil.info(MODULE, "=========未忽略了密码准确性的校验！并且开启了验证码校验  将进行验证码验证==========");
				checkValidateCode(request);
			}
		}else{
			//忽略密码校验并开启了短息验证执行短信验证码校验
			if(smsCode){
				if(ParamConstant.LOGIN_SMS_VALUE.equalsIgnoreCase(request.getParameter(ParamConstant.LOGIN_SSO_KEY))){
					if(captchaCode){
						LogUtil.info(MODULE, "=========忽略了密码准确性的校验！并且开启了验证码校验  将进行验证码验证==========");
						checkValidateCode(request);
					}
					checkSMSCode(request);
				}
			}
		}

		Authentication auth=null;
		try{
			auth=super.attemptAuthentication(request, response);
		}catch(AuthenticationException ex){
			logger.error(ex.getMessage(), ex);
			if(UsernameNotFoundException.class.isAssignableFrom(ex.getClass())){
				throw new AuthenticationServiceException(ex.getMessage(),ex);
			}else if(InternalAuthenticationServiceException.class.isAssignableFrom(ex.getClass())){
				throw new AuthenticationServiceException(ResourceMsgUtil.getMessage("webcore.000013",new Object[]{}),ex);
			}else if(BadCredentialsException.class.isAssignableFrom(ex.getClass())){
				//=====================密码错误次数限制
				if(null!=authRSV){
					String username=obtainUsername(request);
					LoginPwdCntReqDTO dto=new LoginPwdCntReqDTO(username,"0");
					int rs=0;
					try {
						rs=authRSV.updateLoginPwdCnt(dto);
					} catch (BusinessException e) {
						logger.error("修改密码错误次数",e);
						throw new BusinessException(e.getMessage());
					}
					if(rs>0){
						throw new AuthenticationServiceException(ResourceMsgUtil.getMessage("webcore.000018",new Object[]{rs}),ex);
					}else if(rs==0){
						throw new AuthenticationServiceException(ResourceMsgUtil.getMessage("webcore.000017",new Object[]{}),ex);
					}else{
						throw new AuthenticationServiceException(ResourceMsgUtil.getMessage("webcore.000009",new Object[]{}),ex);
					}
				}else{
					throw new AuthenticationServiceException(ResourceMsgUtil.getMessage("webcore.000009",new Object[]{}),ex);
				}

				//=======================
				//                throw new AuthenticationServiceException(ResourceMsgUtil.getMessage("webcore.000009",new Object[]{}),ex);
			}else{
				throw new AuthenticationServiceException(ResourceMsgUtil.getMessage("webcore.000012",new Object[]{}),ex);
			}
		}
		return auth;
	}

	protected void checkValidateCode(HttpServletRequest request) {
		//        HttpSession session = request.getSession();
		//        
		//        String sessionValidateCode = obtainSessionValidateCode(session); 
		//        //让上一次的验证码失效
		//        session.setAttribute(this.checkCodeParameter, null);

		String validateCodeParameter = obtainValidateCodeParameter(request);
		boolean valid=CaptchaServlet.verifyCaptcha(request, validateCodeParameter);
		if(!valid){
			throw new AuthenticationCheckCodeException(ResourceMsgUtil.getMessage("webcore.000010",new Object[]{}));
		}
		//        if (StringUtils.isEmpty(validateCodeParameter) || !validateCodeParameter.equalsIgnoreCase(sessionValidateCode)) {  
		//            throw new AuthenticationCheckCodeException(ResourceMsgUtil.getMessage("webcore.000010",new Object[]{}));  
		//        }
	}
	protected void checkSMSCode(HttpServletRequest request) {
		String sessionId = request.getSession().getId();
		String phoneKey = ParamConstant.PHONE_CHECK_PRE + sessionId;
		//缓存中获取短信验证码
		Object cacheCheckCode = CacheUtil.getItem(phoneKey);
		String checkCode=request.getParameter(ParamConstant.LOGIN_USERNAME_KEY)+request.getParameter(ParamConstant.LOGIN_SMS_CODE);
		boolean valid=String.valueOf(cacheCheckCode).equals(checkCode);
		if(!valid){
			 //throw new BusinessException("验证码错误");
			//此处存在只输出key的bug暂时用下面方法代替
			//throw new AuthenticationCheckCodeException(ResourceMsgUtil.getMessage("webcore.000019",new Object[]{}));
			throw new AuthenticationCheckCodeException("短信验证码错误");
		}
	}

	private String obtainValidateCodeParameter(HttpServletRequest request) {
		return request.getParameter(checkCodeParameter);
	}

	private String obtainSMScode(HttpServletRequest request) {
		return request.getParameter(ParamConstant.LOGIN_SMS_CODE);
	}

	protected String obtainSessionValidateCode(HttpSession session) {
		return (String)session.getAttribute(checkCodeParameter);
	}

	@Override
	protected String obtainPassword(HttpServletRequest request) {

		return request.getParameter(ParamConstant.LOGIN_PASSWORD_KEY);
	}

	@Override
	protected String obtainUsername(HttpServletRequest request) {

		return request.getParameter(ParamConstant.LOGIN_USERNAME_KEY);
	}


	public final String getCheckCodeParameter() {

		return checkCodeParameter;
	}


	public void setCheckCodeParameter(String checkCodeParameter) {

		this.checkCodeParameter = checkCodeParameter;
	}

}
