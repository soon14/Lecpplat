package com.zengshi.ecp.quartz;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class QuartzContextHolder {
	private static ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] {"ecp-quartz-dubbo.xml" });

	public static ApplicationContext getContext() {
		return ctx;
	}

	public static void setCtx(ApplicationContext ctx) {
		QuartzContextHolder.ctx = ctx;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName) {
		return (T) ctx.getBean(beanName);
	}
	
	public static <T> T getBean(Class<T> clazz){
	    return ctx.getBean(clazz);
	}

	public static void closeCtx() {
		((ClassPathXmlApplicationContext) ctx).close();
	}
}
