package com.zengshi.ecp.test.server;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zengshi.ecp.server.front.util.PasswordUtils;
import com.zengshi.paas.utils.Md5Util;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:ecp-service-context.xml")
public class PasswordUtilsTest extends AbstractJUnit4SpringContextTests {
    
    @Resource
    private PasswordUtils passwordUtil;

    @Test
    public void testEncry() {
        String plaintext = "abcd1234";
        System.out.println("====明文：" + plaintext);
        
        String password = passwordUtil.encry(plaintext);
        System.out.println("====密文：" + password);
        
        String md5 = Md5Util.encode("ECPSSEabcd1234");
        System.out.println("====Md5：" + md5);
        
        System.out.println("====check:" + passwordUtil.checkPassword(plaintext, md5));
        
        
    }

}

