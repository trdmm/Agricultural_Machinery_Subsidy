package com.wdnj.xxb.subsidy.util;

import com.dtflys.forest.annotation.*;
import com.dtflys.forest.callback.OnLoadCookie;
import com.dtflys.forest.callback.OnSaveCookie;
import com.wdnj.xxb.subsidy.entity.RequestBody;

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
   * @param url 地址
   * @param onLoadCookie 加载cookie
   * @param onSaveCookie cookie保存
   * @return 网页html
   */
  @GetRequest(url = "${url}",logEnabled = true)
  String openPublishPage(@DataVariable("url") String url, OnSaveCookie onSaveCookie);

  @Post(url = "${url}",logEnabled = true,contentType = "multipart/form-data")
  String clickQuery(@DataVariable("url") String url, @Body RequestBody requestBody, OnLoadCookie onLoadCookie);


  /**
   * 真正开始查询.
   * 从上一个结果的html中获取body内容.<br/>
   * 删除 Cookie: __RequestVerificationToken
   * @param url 地址
   * @param onLoadCookie 加载cookie
   * @return 网页html
   */
  @Post(url = "${url}",contentType = "multipart/form-data")
  String queryList(@DataVariable("url") String url, @Query("pageIndex") int pageIndex, @Body RequestBody requestBody, OnLoadCookie onLoadCookie);

  @Get(url = "https://xizhi.qqoq.net/XZ523d3a78edaa95bb4c438d94e99b0c03.send")
  void sendWXMsg(@Query("title") String title,@Query("content") String content);
}
