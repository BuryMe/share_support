package com.share.support.constant;

/**
 * @author fuxuan
 * @date 2019/6/10 0010 17:15
 * @description
 */
public enum ErrorCode {

    SUCCESS("0000","处理成功"),
    JWT_EXP("9001","token过期"),
    JWT_ERROR("9002","token解密失败")


    ;

    private String code;

    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    ErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
