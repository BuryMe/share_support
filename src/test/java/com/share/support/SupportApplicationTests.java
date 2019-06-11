package com.share.support;

import com.nimbusds.jose.JOSEException;
import com.share.support.constant.ConfigReader;
import com.share.support.token.TokenUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SupportApplicationTests {

//    @Value("${token.secret}")
//    private static String token;
//
//    private static String value;
//
//    @Value("${token.secret}")
//    public void setValue(String v){
//        value = v;
//    }


    @Autowired
    TokenUtil tokenUtil;

    @Test
    public void contextLoads() throws JOSEException, ParseException {
        Map<String, Object> map = new HashMap<>(4);
        map.put("iss", "123456");
        map.put("iat", System.currentTimeMillis());
        map.put("exp", System.currentTimeMillis() + 1000);
        System.out.println(TokenUtil.creatToken(map));
        System.out.println(TokenUtil.valid(TokenUtil.creatToken(map)));

    }

    @Test
    public void test1(){
        System.out.println(ConfigReader.demo);
        System.out.println(ConfigReader.tokenSecret);
    }

}
