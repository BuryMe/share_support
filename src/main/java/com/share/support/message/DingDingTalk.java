package com.share.support.message;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import com.share.support.expection.ServiceExpection;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 钉钉消息服务调用主入口
 *
 * @author fuxuan
 */
@Slf4j
public class DingDingTalk {

    /**
     * 发送dingding 消息
     *
     * @param content   消息内容
     * @param atMobiles @手机号
     * @param isAtAll   是否@所有人
     * @param dingUrl   dingding 机器人url
     */
    public static void sendText(String content, List<String> atMobiles, Boolean isAtAll, String dingUrl) throws ServiceExpection {
        com.dingtalk.api.DingTalkClient client = new DefaultDingTalkClient(dingUrl);
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent(content);
        request.setText(text);
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        at.setAtMobiles(atMobiles);
        at.setIsAtAll(isAtAll.toString());
        request.setAt(at);
        try {
            OapiRobotSendResponse response = client.execute(request);
        } catch (ApiException e) {
            throw new ServiceExpection(e.getErrCode(), e.getErrMsg());
        }
    }

    /**
     * 发送dingding 链接
     *
     * @param title      消息标题
     * @param text       消息内容。如果太长只会部分展示
     * @param messageUrl 点击消息跳转的URL 需要加 http
     * @param picUrl     图片URL
     * @param dingUrl    dingding 机器人url
     */
    public static void sendLink(String title, String text, String messageUrl, String picUrl, String dingUrl) throws ServiceExpection {
        com.dingtalk.api.DingTalkClient client = new DefaultDingTalkClient(dingUrl);
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("link");
        OapiRobotSendRequest.Link link = new OapiRobotSendRequest.Link();
        link.setMessageUrl(messageUrl);
        link.setPicUrl(picUrl);
        link.setTitle(title);
        link.setText(text);
        request.setLink(link);
        try {
            OapiRobotSendResponse response = client.execute(request);
        } catch (ApiException e) {
            throw new ServiceExpection(e.getErrCode(), e.getErrMsg());
        }
    }

    /**
     * 发送dingding markdown
     *
     * @param title     首屏会话透出的展示内容
     * @param text      markdown格式的消息
     * @param atMobiles 被@人的手机号(在text内容里要有@手机号)
     * @param isAtAll   @所有人时：true，否则为：false
     * @param dingUrl   dingding 机器人url
     */
    public static void sendMarkdown(String title, String text, List<String> atMobiles, Boolean isAtAll, String dingUrl) throws ServiceExpection {
        com.dingtalk.api.DingTalkClient client = new DefaultDingTalkClient(dingUrl);
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("markdown");
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle(title);
        markdown.setText(text);
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        at.setAtMobiles(atMobiles);
        at.setIsAtAll(isAtAll.toString());
        request.setAt(at);
        request.setMarkdown(markdown);
        try {
            OapiRobotSendResponse response = client.execute(request);
        } catch (ApiException e) {
            throw new ServiceExpection(e.getErrCode(), e.getErrMsg());
        }
    }

    public static void main(String[] args) {

//        DingDingTalk.SendDingText("demo",null,false,"https://oapi.dingtalk.com/robot/send?access_token=bc4129db9e332cac26f9d8304c152a48f8d532a1c72a1fad5c92142f8b57e399");
//        DingDingTalk.SendDingText("demo",null,true,"https://oapi.dingtalk.com/robot/send?access_token=bc4129db9e332cac26f9d8304c152a48f8d532a1c72a1fad5c92142f8b57e399");
//        DingDingTalk.SendDingLink("demo title","demo text","http://www.baidu.com","","https://oapi.dingtalk.com/robot/send?access_token=bc4129db9e332cac26f9d8304c152a48f8d532a1c72a1fad5c92142f8b57e399");
    }
}
