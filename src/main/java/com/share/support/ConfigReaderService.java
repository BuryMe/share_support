package com.share.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.LoaderUtil;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * @author fuxuan
 * @date 2019/6/10 0010 15:05
 * @description
 */
@Slf4j
@Service
public class ConfigReaderService {

    public static String tokenSecret;

//    public static String getTokenSecret() {
//        return tokenSecret;
//    }
//
//    @Value("${token.secret}")
//    public static void setTokenSecret(String tokenSecret) {
//        ConfigReaderService.tokenSecret = tokenSecret;
//    }

//    @Autowired
//    Environment environment;
//
//    @PostConstruct
//    public void readConfig() throws IllegalAccessException {
//        String prefix = "config";
//        Field[] fields = ConfigReaderService.class.getFields();
//        for (Field field : fields) {
//            field.set(null, getPropertyValue(prefix + field.getName()));
//        }
//    }
//
//    private String getPropertyValue(String keyName) {
//        return environment.getProperty(keyName);
//    }

    static {
        ConfigReaderService configReaderService = new ConfigReaderService();
        log.info("------- static begin -----------------");
        Properties application = null;
        try {
            application = PropertiesLoaderUtils.loadAllProperties("application.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("--------------" + application.getProperty("config.token.secret"));
        String prefix = "config";
        Field[] fields = ConfigReaderService.class.getFields();
        for (Field field : fields) {
            try {
                field.set(configReaderService, application.getProperty(prefix + field.getName()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        log.info("------- static end -----------------");
    }

}
