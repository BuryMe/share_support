package com.share.support.controller;

import com.share.support.http.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author fuxuan
 * @date 2019/6/11 0011 14:18
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class Test {

    @PostMapping("/thirdApi")
    public void thirdApi(HttpServletRequest request) {
        log.info("------ 模拟第三方接收接口 -------");

        try{
//            HttpClient.synchronizeRequest("SPost","http://localhost:8080/test/myCallBack","",null);
        }catch (Exception e){
            log.error("http失败",e);
        }

    }

    @PostMapping("/myCallBack")
    public void myCallBack() {
        log.info("------模拟我方接收第三方回调接口-------");
    }


}
