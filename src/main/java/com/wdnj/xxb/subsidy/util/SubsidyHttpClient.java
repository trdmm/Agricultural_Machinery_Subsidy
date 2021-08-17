package com.wdnj.xxb.subsidy.util;

import com.dtflys.forest.annotation.*;
import com.dtflys.forest.callback.OnLoadCookie;
import com.dtflys.forest.callback.OnSaveCookie;
import com.wdnj.xxb.subsidy.entity.ding_talk.DingTalkMsg;
import com.wdnj.xxb.subsidy.entity.subsidyInfo.RequestBody;

/**
 * 描述:...<br>
 *
 * @version 1.0.0
 * @project: Subsidy
 * @file: HttpClient
 * @author: Mingming Huang
 * @since: 2021/3/3 17:53
 */
public interface SubsidyHttpClient {
    /**
     * 打开 公示页,获取请求的body内数据
     *
     * @param url          地址
     * @param onLoadCookie 加载cookie
     * @param onSaveCookie cookie保存
     * @return 网页html
     */
    @GetRequest(url = "${url}", logEnabled = true)
    String openPublishPage(@DataVariable("url") String url, OnSaveCookie onSaveCookie);

    @Post(url = "${url}", logEnabled = true, contentType = "multipart/form-data")
    String clickQuery(@DataVariable("url") String url, @Body RequestBody requestBody, OnLoadCookie onLoadCookie);


    /**
     * 真正开始查询.
     * 从上一个结果的html中获取body内容.<br/>
     * 删除 Cookie: __RequestVerificationToken
     *
     * @param url          地址
     * @param onLoadCookie 加载cookie
     * @return 网页html
     */
    @Post(url = "${url}", contentType = "multipart/form-data")
    String queryList(@DataVariable("url") String url, @Query("pageIndex") int pageIndex, @Body RequestBody requestBody, OnLoadCookie onLoadCookie);


    /**
     * 发送钉钉机器人消息
     * @param msg 钉钉消息(消息内容必须要包含"沃得")
     */
    @Post(url = "https://oapi.dingtalk.com/robot/send?access_token=5db19a64bd908da40defb2a35e514372c2abb9e340b57d0fdaf33aba00a71752")
    void sendDingTalkMsg(@JSONBody DingTalkMsg msg);

    //@Get(url = "${url}", contentType = "multipart/form-data")
    @Get(url = "${url}")
    String queryFhInfo(@DataVariable("url") String url,@Query("pageIndex") int pageIndex, @Query com.wdnj.xxb.subsidy.entity.factoryFhInfo.RequestBody requestBody);

    /**
     * 获取最新的url
     * @param url 请求连接的地址
     * @return JS文件
     */
    @Get(url = "${url}")
    String getLatestUrl(@DataVariable("url") String url);
}
