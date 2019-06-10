package com.share.support.constant;

/**
 * @author fuxuan
 * @date 2019/6/10 0010 17:04
 * @description
 */
public enum JWTEnum {

    ISS("iss","jwt签发者"),
    SUB("sub","jwt所面向的用户"),
    AUD("aud","接收jwt的一方"),
    EXP("exp","jwt的过期时间，这个过期时间必须要大于签发时间"),
    NBF("nbf","定义在什么时间之前，该jwt都是不可用的"),
    IAT("iat","jwt的签发时间"),
    JTI("jti","jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击");

    private String name;

    private String text;

    JWTEnum(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
