package com.zengshi.ecp.server.dubbo;

import com.zengshi.paas.utils.PaasContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zengshi.ecp.frame.context.EcpFrameContextHolder;

public class DubboServiceStart {

    private static final Logger log = LoggerFactory.getLogger(DubboServiceStart.class);

    public static final String ECP_DUBBO_PROVIDER_XML = "classpath*:ecp-service-context.xml";

    public static void init() {
        System.setProperty("oracle.jdbc.V8Compatible", "true");
        /**
         * 加载paasContext.xml
         */
        PaasContextHolder.init();

        log.info("开始启动dubo服务，载入的配置服务提供文件为[" + ECP_DUBBO_PROVIDER_XML + "]");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] { ECP_DUBBO_PROVIDER_XML });
        context.registerShutdownHook();
        context.start();
        EcpFrameContextHolder.setContext(context);
        log.info("dubbo启动服务完毕，请查看日志");
    }

    public static void main(String[] args) {

        boolean needLoadParams = false;
        if (null != args && args.length >= 1) {
            String[] splits = args[0].trim().split(":");
            if (null != splits && splits.length >= 2) {
                if (splits[0].equalsIgnoreCase("loadParams") && splits[1].equalsIgnoreCase("true")) {
                    needLoadParams = true;
                }
            }
        }

        long t1 = System.currentTimeMillis(), t2 = 0L;
        try{
            DubboServiceStart.init();
        } catch(Exception err){
            log.error("后场服务初始化异常", err);
            log.info("服务启动异常；暂停服务，中止--pause");
            System.exit(0);
        }
        
        t2 = System.currentTimeMillis();

        /**
         * 加载参数； add by yugn 2014.8.11
         */
        /*
         * if (needLoadParams) { LoadAllParam2CacehStart.loadParam(); }
         */
        // System.out.println(PaasContextHolder.getBean(AipSvcName.WoegoWsClientService));
        // System.out.println(PaasContextHolder.getBean(AipSvcName.OrderQueryService));
        log.info("后厂启动总耗时:" + (System.currentTimeMillis() - t1) + "ms" + ",其中加载服务耗时:"
                + (t2 - t1) + "ms,加载缓存耗时:" + (System.currentTimeMillis() - t2) + "ms");
        while (true) {
            try {
                Thread.currentThread();
                Thread.sleep(3L);
            } catch (Exception e) {
                //e.printStackTrace();
                log.error("后场服务运行异常", e);
            }
        }

    }

}
