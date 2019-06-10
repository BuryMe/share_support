package com.share.support.token;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import com.share.support.constant.ErrorCode;
import com.share.support.constant.JWTEnum;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author fuxuan
 * @date 2019/6/10 0010 11:06
 * @description
 */
@Slf4j
@Component
public class TokenUtil {

    private static String value;

    @Value("${config.token.secret}")
    public void setValue(String v) {
        value = v;
    }

    public static String creatToken(Map<String, Object> payloadMap) throws JOSEException {
        //3.先建立一个头部Header
        /**
         * JWSHeader参数：1.加密算法法则,2.类型，3.。。。。。。。
         * 一般只需要传入加密算法法则就可以。
         * 这里则采用HS256
         * JWSAlgorithm类里面有所有的加密算法法则，直接调用。
         */
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        //建立一个载荷Payload
        Payload payload = new Payload(new JSONObject(payloadMap));
        //将头部和载荷结合在一起
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        //建立一个密匙
        JWSSigner jwsSigner = new MACSigner(value.getBytes());
        //签名
        jwsObject.sign(jwsSigner);
        //生成token
        return jwsObject.serialize();
    }

    /**
     * @param token
     * @return
     * @throws ParseException
     * @throws JOSEException
     */
    public static Map<String, Object> valid(String token) throws ParseException, JOSEException {
//        解析token
        JWSObject jwsObject = JWSObject.parse(token);
        //获取到载荷
        Payload payload = jwsObject.getPayload();
        //建立一个解锁密匙
        JWSVerifier jwsVerifier = new MACVerifier(value.getBytes());
        Map<String, Object> resultMap = new HashMap<>(4);
        //判断token
        if (jwsObject.verify(jwsVerifier)) {
            //载荷的数据解析成json对象。
            JSONObject jsonObject = payload.toJSONObject();
            Long expTime = Long.valueOf(jsonObject.get(JWTEnum.EXP.getName()).toString());
            Long nowTime = System.currentTimeMillis();
            Boolean isExpired = false;
            if (nowTime > expTime) {
                isExpired = true;
            }
            resultMap.put("code", (isExpired) ? ErrorCode.JWT_EXP.getCode() : ErrorCode.SUCCESS.getCode());
            resultMap.put("msg", (isExpired) ? ErrorCode.JWT_EXP.getMsg() : ErrorCode.SUCCESS.getMsg());
            resultMap.put("data", (isExpired) ? null : jsonObject);
        } else {
            resultMap.put("code", ErrorCode.JWT_ERROR.getCode());
            resultMap.put("msg", ErrorCode.JWT_ERROR.getMsg());
            resultMap.put("data", null);
        }
        return resultMap;
    }


}
