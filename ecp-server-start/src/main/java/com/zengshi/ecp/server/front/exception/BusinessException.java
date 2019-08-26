package com.zengshi.ecp.server.front.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zengshi.paas.utils.LocaleUtil;
import com.zengshi.paas.utils.ResourceMsgUtil;
import com.zengshi.paas.utils.ThreadId;


public class BusinessException extends GenericException {

    private static final long serialVersionUID = 1L;

    private static Log log = LogFactory.getLog(BusinessException.class);
    
    public BusinessException(){
        super();
    }

    public BusinessException(String errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
        this.errorMessage = ResourceMsgUtil.getMessage(errorCode, null, LocaleUtil.getLocale());
        this.writeBusinessException();
    }

    public BusinessException(String errorCode, String[] keyInfos) {
        super(errorCode);
        this.errorCode = errorCode;
        this.errorMessage = ResourceMsgUtil.getMessage(errorCode, keyInfos, LocaleUtil.getLocale());
        this.writeBusinessException();
    }

    public BusinessException(String errorCode ,Throwable e){
        super(errorCode,e);
        this.errorCode = errorCode;
        this.errorMessage = ResourceMsgUtil.getMessage(errorCode, null, LocaleUtil.getLocale());
        this.writeBusinessException();
    }

    public BusinessException(String errorCode, String[] keyInfos, Throwable e) {
        super(errorCode,e);
        this.errorCode = errorCode;
        this.errorMessage = ResourceMsgUtil.getMessage(errorCode, keyInfos, LocaleUtil.getLocale());
        this.writeBusinessException();
    }

    private void writeBusinessException() {
        log.error("线程："+ThreadId.getThreadId()+" ; 业务异常编码：" + this.getErrorCode() + " ；业务异常信息：" + this.getErrorMessage());
    }
}
