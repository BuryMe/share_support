package com.share.support.constant;

import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;

/**
 * 配置信息读取类
 * @author fuxuan
 * @date 2019/6/10 0010 18:05
 * @description
 */
@Slf4j
@Component
public class ConfigReader {
    // 随便写点，测试远程仓库连接是否顺畅 --- by zian
    // 随便写点，测试远程仓库连接是否顺畅 --- by zian
    // 随便写点，测试远程仓库连接是否顺畅 --- by zian
//    private final Logger log = LoggerFactory.getLogger(this.getClass());


    public static String demo;

    public static String tokenSecret;

    @Autowired
    Environment environment;

    @PostConstruct
    public void readConfig() throws IllegalAccessException {
        ConfigReader configReaderService = new ConfigReader();
        Field[] fields = ConfigReader.class.getFields();
        for (Field field : fields) {
            String name = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, field.getName());
            log.info(name);
            String propertyName = name.replaceAll("-",".");
            log.info(propertyName);
            field.set(configReaderService, getPropertyValue( propertyName));

        }
    }

    private String getPropertyValue(String keyName) {
        return environment.getProperty(keyName);
    }

    public static void main(String[] args) {
        System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, "testData"));
    }
}
