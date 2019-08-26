package com.zengshi.ecp.server.front.exception;

import java.io.Serializable;

public class GenericException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    String errorCode;

    String errorMessage;

    //Exception errOri;

    @Override
    public String getMessage() {
        return this.getErrorMessage();
    }

    public GenericException() {
        super();
    }
    
    public GenericException(String errCode) {
        this.errorCode = errCode;
    }

    public GenericException(String errCode, Throwable e) {
        super(e);
        this.errorCode = errCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /*public Exception getErrOri() {
        return errOri;
    }

    public void setErrOri(Exception errOri) {
        this.errOri = errOri;
    }*/

}
