/** 
 * Date:2015-8-26下午4:23:48 
 * 
 */ 
package com.zengshi.ecp.base.filter;

import com.zengshi.ecp.base.util.ApplicationContextUtil;
import com.zengshi.ecp.base.util.ParamConstant;
import com.zengshi.ecp.base.util.WebContextUtil;
import com.zengshi.ecp.server.front.dto.BaseStaff;
import com.zengshi.ecp.server.front.security.AuthPrivilegeResDTO;
import com.zengshi.ecp.server.front.util.SiteLocaleUtil;
import com.zengshi.ecp.server.front.util.StaffLocaleUtil;
import com.zengshi.paas.session.SessionManager;
import com.zengshi.paas.utils.CacheUtil;
import com.zengshi.paas.utils.LocaleUtil;
import com.zengshi.paas.utils.PaasContextHolder;
import org.drools.core.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

/**
 * Description: <br>
 * Date:2015-8-26下午4:23:48  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class LocaleFilter implements Filter {

	private String[] ignoreSuffix = {};

	private ISiteHandler siteHandler;

	// 会员信息
	private static final String StaffClass_C = "20";

	//补充了一个入参：用户ID；
	private String cksId;

	/**
	 * 用户类别
	 * @param staffClass
	 */
	/*
    public void setStaffClass(String staffClass) {
        this.staffClass = staffClass;
    }
	 */

	/**
	 * 初始化过滤器，屏蔽部分的请求；
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String ignore_suffix = filterConfig.getInitParameter("ignore_suffix");
		if (!"".equals(ignore_suffix))
			ignoreSuffix = ignore_suffix.split(",");

		///初始化站点 处理 方法；
		String siteHandlerName = filterConfig.getInitParameter("site_handler");
		//        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
		if(StringUtils.isEmpty(siteHandlerName)){
			siteHandler = ApplicationContextUtil.getBean("defaultSiteHandler", ISiteHandler.class);
		} else {
			siteHandler = ApplicationContextUtil.getBean(siteHandlerName, ISiteHandler.class);
		}

		///用于处理写入cookie的用户ID内容；
		cksId = filterConfig.getInitParameter("cks_id");
		if(StringUtils.isEmpty(cksId)){
			cksId = ParamConstant.CK_USER_ID;
		}
	}

	/**
	 * TODO 用于初始化一些 线程变量信息，包括：区域信息、登录用户信息、
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpReq = (HttpServletRequest)request;
		///判断是否过滤部分请求；
		if (!shouldFilter(httpReq)) {
			chain.doFilter(request, response);
			return;
		}

		/**
		 * 设置国际化的 区域信息； 如果初始Session中的信息为空，那么获取Default;
		 */
		/*Object obj = httpReq.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
        Locale locale;
        if(obj == null){
            locale = Locale.getDefault();
            httpReq.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
        } else {
            locale = (Locale)obj;
        }*/
		//所有的语言，统一设置为中文；
		LocaleUtil.setLocale(Locale.SIMPLIFIED_CHINESE);

		/**
		 * 从Session中获取 Staff 信息；待补充
		 * TODO
		 */
		AuthPrivilegeResDTO dto = WebContextUtil.getCurrentUser();
		BaseStaff staff = new BaseStaff();
		if(dto == null){
			//未登录的时候，初始化一个ID 为0L，的会员；帐号：UUID随机 扣除-
			staff.setStaffClass(StaffClass_C); //设置为会员；
			staff.setId(0L);
			staff.setStaffCode(UUID.randomUUID().toString().replaceAll("-", ""));
			//未登录,设置默认的会员等级为 "01";
			staff.setStaffLevelCode("01");
		} else {
			//用户锁定后强制退出登录 by zp 2018-12-03
			Boolean staffId = (Boolean)CacheUtil.getItem(ParamConstant.USER_LOCK+dto.getStaffId());
			if(staffId!=null&&staffId){
				HttpServletResponse httpRes=(HttpServletResponse)response;
				WebContextUtil.logout(httpReq, httpRes);
				//未登录的时候，初始化一个ID 为0L，的会员；帐号：UUID随机 扣除-
				staff.setStaffClass(StaffClass_C); //设置为会员；
				staff.setId(0L);
				staff.setStaffCode(UUID.randomUUID().toString().replaceAll("-", ""));
				//未登录,设置默认的会员等级为 "01";
				staff.setStaffLevelCode("01");
			}else{
				staff.setId(dto.getStaffId());
				staff.setStaffClass(dto.getStaffClass());
				staff.setStaffCode(dto.getStaffCode());
				staff.setStaffLevelCode(dto.getCustLevelCode());
			}
		}
		StaffLocaleUtil.setStaff(staff);
		///设置站点信息；
		if(siteHandler == null){
			SiteLocaleUtil.setSite(0L);
		} else {
			SiteLocaleUtil.setSite(siteHandler.doHandler(httpReq));
		}

		///写入response cookie;
		this.saveUserIdToCookie( (HttpServletRequest)request, (HttpServletResponse)response);

		chain.doFilter(request, response);

	}

	///将cookie信息写入 request;
	private void saveUserIdToCookie(HttpServletRequest request, HttpServletResponse response){

		BaseStaff staff = StaffLocaleUtil.getStaff();
		//表示：如果staff == null ,那么取uuid
		//  否则,如果staff登录（staff.getId <1 ）,那么取staff.getStaffCode() ，原来默认写入的时候，也是UUID;
		///如果已经登录了，那么就获取 staffId;

		String baseStaffId = ((staff==null||staff.getId()<1)?request.getSession().getId():staff.getId()+"");

		Cookie cookie = null;
		/**
		 * 如果request中没有这个cookie，那么新建一个；如果有，则用原来的；
		 */
		Cookie[] cookies = request.getCookies();
		if ((cookies == null) || (cookies.length == 0)){
			cookie = new Cookie(cksId,baseStaffId);
		} else {

			if(staff==null||staff.getId()<1){
				///未登录，则不用赋值；并且直接退出；
				//表示的cookie下有这个值；不需要修改，并且不需要重新设置userId;
				return;
			}

			for (Cookie ck : cookies) {
				if (cksId.equals(ck.getName())){
					cookie = ck;
					//登录；并且当前的用户Id和cookie的值一致，那么也不需要重新设置；直接退出；
					if((staff.getId()+"").equalsIgnoreCase(ck.getValue())){
						return;
					} else {
						cookie.setValue(staff.getId()+"");
					}
					break;
				}
			}

			//如果经过上面的循环之后，还是为空；表示request有cookie信息，但没有 key 是 cksId 的cookie;
			//那么新建这个cookie;
			if(cookie == null){
				cookie = new Cookie(cksId,baseStaffId);
			}
		}

		cookie.setMaxAge(180*24*60*60); //有效期设置为 180天；

		//获取context;
		SessionManager manager = PaasContextHolder.getContext().getBean("sessionManager", SessionManager.class);
		manager.saveCookieToResponse(cookie, request, response);

	}
	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * @see Filter#destroy()
	 */
	@Override
	public void destroy() {
	}

	private boolean shouldFilter(HttpServletRequest request) {
		String uri = request.getRequestURI().toLowerCase();
		for (String suffix : ignoreSuffix) {
			if (uri.endsWith(suffix))
				return false;
		}
		return true;
	}



}

