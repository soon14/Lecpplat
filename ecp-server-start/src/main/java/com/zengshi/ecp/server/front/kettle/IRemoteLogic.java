package com.zengshi.ecp.server.front.kettle;

/**
 */
public interface IRemoteLogic {
    /**
     * 对数据做业务逻辑判断
     * @param data
     * @return 返回结果
     * @throws Exception
     */
    boolean doLogic(Object data) throws Exception;
}
