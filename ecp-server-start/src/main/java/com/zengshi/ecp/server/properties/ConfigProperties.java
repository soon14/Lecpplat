package com.zengshi.ecp.server.properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 */
public class ConfigProperties {
    private final static Logger logger= Logger.getLogger(ConfigProperties.class);

    private Properties props;

    public ConfigProperties(){
        try {
            Resource res=new ClassPathResource("config.properties");
            props=new Properties();
            props.load(res.getInputStream());
//            props = PropertiesLoaderUtils.loadAllProperties("config.properties");
        } catch (IOException e) {
            logger.error("加载config.properties文件失败!",e);
        }
    }

    public String getValue(String key){
        String value=props.getProperty(key);

        if(!StringUtils.hasText(value)){
            return value;
        }
        String reg="\\$\\{(.+)\\}";
        Pattern pattern=Pattern.compile(reg);
        Matcher matcher=pattern.matcher(value);
        if(matcher.find()){
            int count=matcher.groupCount();
                for(int i=1;i<=count;i++){
                    String sub=matcher.group(i);
                String subValue=getValue(sub);
                value=value.replaceAll(reg,subValue);
            }
        }
        return value;
    }

    public String getValue(String key,String defValue){
        String value=getValue(key);


        if(!StringUtils.hasText(value)){
            return defValue;
        }

        return value;
    }

}
