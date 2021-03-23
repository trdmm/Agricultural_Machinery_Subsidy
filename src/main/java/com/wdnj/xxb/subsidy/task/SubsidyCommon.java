package com.wdnj.xxb.subsidy.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.excel.EasyExcel;
import com.dtflys.forest.http.ForestCookie;
import com.wdnj.xxb.subsidy.entity.RequestBody;
import com.wdnj.xxb.subsidy.entity.SubsidyInfo;
import com.wdnj.xxb.subsidy.util.DocumentUtil;
import com.wdnj.xxb.subsidy.util.SubsidyHttpClient;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 描述:...<br>
 *
 * @version 1.0.0
 * @project: Subsidy
 * @file: SubsidyCommon
 * @author: Mingming Huang
 * @since: 2021/3/18 14:52
 */
@Slf4j
public class SubsidyCommon {
    /**
     * 查询补贴数据
     * 
     * @param region
     *            地区
     * @param years
     *            年份数组
     * @param httpClient
     *            http客户端
     * @param publicUrl
     *            公示页URL
     * @param listUrl
     *            查询结果页URL
     * @param dir
     *            本次查询目录file
     */
    public static void getSubsidy(String region, int[] years, SubsidyHttpClient httpClient, String publicUrl,
        String listUrl, File dir) {
        /* 存放cookie */
        List<ForestCookie> cookies = new ArrayList<>();
        /* 打开公示页 */
        String publishPage = httpClient.openPublishPage(publicUrl,
            (forestRequest, forestCookies) -> cookies.addAll(forestCookies.allCookies()));
        /* 获取页面中的token */
        String token = DocumentUtil.extractBodyCookie(publishPage);
        /* 存放补贴信息 */
        List<SubsidyInfo> list = new ArrayList<>();
        /* 计数 */
        // AtomicInteger atomicInteger = new AtomicInteger();

        for (int year : years) {

            /* 地区年份文件夹 */
            File mkdir = FileUtil.mkdir(FileUtil.getAbsolutePath(dir) + "/" + region + "/" + year);

            /* 表单内容 */
            RequestBody body = RequestBody.builder().year(String.valueOf(year)).requestVerificationToken(token).build();
            /* 点击公示页查询结果 */
            String clickQueryResult = httpClient.clickQuery(listUrl, body,
                (forestRequest, forestCookies) -> forestCookies.addAllCookies(cookies));
            /* 提取总页数 */
            int pages = DocumentUtil.extractPages(clickQueryResult);
            log.info("{} {} 年总共{}页", region, year, pages);
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            for (int i = 1; i <= pages; i++) {
                /* 正式开始查询 */
                String result = httpClient.queryList(listUrl, i, body,
                    (forestRequest, forestCookies) -> forestCookies.addAllCookies(cookies));
                /* 解析html中的补贴数据 */
                try {
                    List<SubsidyInfo> subsidyInfos = DocumentUtil.htmlToList(result);
                    /* 解析的数据加入列表 */
                    list.addAll(subsidyInfos);
                    if (i % 15 == 0) {
                        EasyExcel.write(FileUtil.getCanonicalPath(mkdir) + "/补贴车辆-" + i + ".xlsx", SubsidyInfo.class)
                            .sheet().doWrite(list);
                        list.clear();
                        ThreadUtil.safeSleep(15 * 1000);
                    } else {
                        ThreadUtil.safeSleep(800);
                    }
                } catch (Exception e) {
                    i--;
                    log.error("{} {} 年,第 {} 页出错,休眠3min",region,year,i);
                    ThreadUtil.safeSleep(3 * 60 * 1000);
                }
            }
            stopWatch.stop();
            log.warn("{} {} 年已完成,耗时{}ms.", region, year, stopWatch.getLastTaskTimeMillis());
            EasyExcel.write(FileUtil.getCanonicalPath(mkdir) + "/补贴车辆-" + pages + ".xlsx", SubsidyInfo.class).sheet()
                .doWrite(list);
            list.clear();
            httpClient.sendWXMsg(region+" "+year+" over","共耗时"+stopWatch.getLastTaskTimeMillis()+"ms");
            // 一年结束 停 3min
            ThreadUtil.safeSleep(3 * 60 * 1000);
        }
    }
}
