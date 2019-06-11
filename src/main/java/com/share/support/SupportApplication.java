package com.share.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
public class SupportApplication {

    public static void main(String[] args) {
        log.info("---------share_support启动开始----------");
        SpringApplication.run(SupportApplication.class, args);
        log.info("---------share_support启动成功----------");

    }

}
