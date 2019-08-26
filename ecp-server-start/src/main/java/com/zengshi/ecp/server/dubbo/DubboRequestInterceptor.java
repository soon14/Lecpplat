package com.zengshi.ecp.server.dubbo;

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import com.zengshi.ecp.server.front.constans.ExptCodeConstants;
import com.zengshi.ecp.server.front.dto.BaseInfo;
import com.zengshi.ecp.server.front.dto.BaseStaff;
import com.zengshi.ecp.server.front.exception.BusinessException;
import com.zengshi.ecp.server.front.util.SiteLocaleUtil;
import com.zengshi.ecp.server.front.util.StaffLocaleUtil;
import com.zengshi.paas.utils.LocaleUtil;
import com.zengshi.paas.utils.ThreadId;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Description: DUBBO请求拦截器；用于提供请求服务拦截处理； 用于日志记录；<br>
 *    取消了执行结束之后的 清理线程变量，改为在初始执行之前先清理线程变量，而后，再用参数进行覆盖处理；
 * Date: 2015年7月21日 <br>
 * 
 */
public class DubboRequestInterceptor {

    private static Log log = LogFactory.getLog(DubboRequestInterceptor.class);

    public void interceptorDubboReq(JoinPoint jp) {
        
        String reqSV = jp.getTarget().getClass().getName();
        String reqMethod = jp.getSignature().getName();
        log.error("开始拦截dubbo服务接口[" + reqSV + "]，方法[" + reqMethod + "]请求..");
        
        /**
         * 清理线程变量； 在执行之前清理该线程内的变量；之后，从参数中获取，并进一步初始化线程变量；
         * add by yugn;
         */
        clearThreaLocale();
        
        Object[] args = jp.getArgs();
        JSONObject p = new JSONObject();
        if (args == null || args.length == 0) {
            log.debug("拦截dubbo服务接口[" + reqSV + "]，方法[" + reqMethod + "]没有入参..");
        } else {
            int i = 0;
            for (Object o : args) {
                String param = null;
                if (o != null) {
                    
                    //如果入参是 BaseInfo，需要初始化一些线程变量；
                    if (o instanceof BaseInfo) {
                        initThreadLocale((BaseInfo) o);
                    }

                    try {
                        param = JSON.toJSONString(o);
                    } catch (Exception e) {
                        try {
                            log.error("参数：" + param + "，可能是包含乱码。try toJson in sencond way...");
                            param = net.sf.json.JSONObject.fromObject(o).toString();
                        } catch (Exception ee) {
                            param = o.toString();
                            log.error("没办法了就是解析不了", ee);
                        }
                    }

                }
                log.error("localThreadId:["+ThreadId.getThreadId()+"];拦截dubbo服务接口[" + reqSV + "]，方法[" + reqMethod + "]的入参" + i + "=【" + param
                        + "】..");
                p.put("P_" + i, param);
            }
            log.error(p);
        }

    }

    public Object aroundDubboSrv(ProceedingJoinPoint pjp) throws Throwable {
        return null;
    }

    /**
     * 线程变量清理，dubbo使用的是线程池；但是线程的线程变量不会自动清理，所以需要在服务调用之后，手动清理；
     * 
     * @param jp
     */
    public void afterDubboReq(JoinPoint jp) {
        /***
         * 此处，不能清理线程变量；目前存在，Dubbo服务，调用Dubbo服务的情况，如果在该种情况下，将导致线程变量被清理；造成，后续的不可用；
         * 
         * modify by yugn 2015.11.15
         */
        /*if (log.isDebugEnabled()) {
            log.debug("localThreadId:["+ThreadId.getThreadId()+"];线程变量清理，设置其中的threadId为空");
        }
        clearThreaLocale();*/
    }
    
    /**
     * 
     * afterThrowing: 在执行的方法有异常的时候处理；
     *  如果Exception 是BusinessException 那么 不处理；
     *  其它异常 需要封装为 BusinessException； 
     * @param jp
     * @param exception 
     * @since JDK 1.6
     */
    public void afterThrowing(JoinPoint jp, Exception exception) throws BusinessException{
        if (log.isDebugEnabled()) {
            log.debug("localThreadId:["+ThreadId.getThreadId()+"];Dubbo服务有异常；开始处理异常");
        }
        
        String reqSV = jp.getTarget().getClass().getName();
        String reqMethod = jp.getSignature().getName();
        log.error("localThreadId:["+ThreadId.getThreadId()+"];throw exception;dubbo服务接口[" + reqSV + "." + reqMethod + "];",exception);
        
        if(exception instanceof BusinessException){
            throw (BusinessException)exception;
        } else {
            ///是其它异常，统一处理为：内部服务异常；
            throw new BusinessException(ExptCodeConstants.Special.SYSTEM_ERROR_OTHER);
        }
    }
    
    
    /**
     * 
     * initThreadLocale: 根据入参，初始化线程变量<br/> 
     * 
     * @param info 
     * @since JDK 1.6
     */
    private void initThreadLocale(BaseInfo info){
        ThreadId.setThreadId(info.getThreadId());
        LocaleUtil.setLocale(info.getLocale());
        StaffLocaleUtil.setStaff(info.getStaff());
        SiteLocaleUtil.setSite(info.getCurrentSiteId());
    }
    
    /**
     * 
     * clearThreaLocale:清理线程变量<br/> 
     * 
     * @since JDK 1.6
     */
    private void clearThreaLocale(){
        if (log.isDebugEnabled()) {
            log.debug("===清理线程变量====，默认设置为中文字符集");
        }
        ThreadId.setThreadId("");
        LocaleUtil.setLocale(Locale.SIMPLIFIED_CHINESE);
        StaffLocaleUtil.setStaff(new BaseStaff());
        SiteLocaleUtil.setSite(null);
    }
}
