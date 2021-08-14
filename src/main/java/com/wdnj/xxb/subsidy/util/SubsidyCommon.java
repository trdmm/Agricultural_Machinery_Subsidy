package com.wdnj.xxb.subsidy.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.dtflys.forest.exceptions.ForestNetworkException;
import com.dtflys.forest.http.ForestCookie;
import com.wdnj.xxb.subsidy.entity.factoryFhInfo.FhInfo;
import com.wdnj.xxb.subsidy.entity.subsidyInfo.RequestBody;
import com.wdnj.xxb.subsidy.entity.subsidyInfo.SubsidyInfo;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import com.wdnj.xxb.subsidy.entity.subsidyInfo.YearInfo;
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
     * @param region     地区
     * @param httpClient http客户端
     * @param yearInfos 年份+url 信息
     * @param dir        本次查询目录file
     */
    public static void getSubsidy(String region, SubsidyHttpClient httpClient, Collection<YearInfo> yearInfos, File dir) {
        for (YearInfo yearInfo : yearInfos) {
            int year = yearInfo.getYear();
            // 公示地址
            String publicUrl = yearInfo.getUrl();
            // 点击搜索后地址
            String listUrl = publicUrl.replace("gongshi", "GongShiSearch");

            /* 存放cookie */
            List<ForestCookie> cookies = new ArrayList<>();
            /* 打开公示页 */
            String publishPage = httpClient.openPublishPage(publicUrl,
                (forestRequest, forestCookies) -> cookies.addAll(forestCookies.allCookies()));
            /* 获取页面中的token */
            String token = DocumentUtil.extractBodyCookie(publishPage);
            if (token == null){
                // 本年度无数据
                continue;
            }

            if (StrUtil.isBlank(token)) {
                // 没获取到 token,可能逻辑已改变,发送消息
                httpClient.sendWXMsg(region + "-" + year + "年获取token失败","请查看是否逻辑已修改");
                continue;
            }

            /* 存放补贴信息 */
            List<SubsidyInfo> list = new ArrayList<>();
            /* 计数 */
            // AtomicInteger atomicInteger = new AtomicInteger();

            /* 地区年份文件夹 */
            File mkdir = FileUtil.mkdir(FileUtil.getAbsolutePath(dir) + "/" + region + "/" + year);

            // 动力机械 14
            /* 表单内容 */
            RequestBody body = RequestBody.builder()
                .year(String.valueOf(year))
                .requestVerificationToken(token)
                // .jiJuLeiXing("收获机械")
                // .jiJuLeiXingCode("04")
                // .jiJuLeiXing("动力机械")
                // .jiJuLeiXingCode("14")
                .build();
            /* 点击公示页查询结果 */
            String clickQueryResult = httpClient.clickQuery(listUrl, body,
                (forestRequest, forestCookies) -> forestCookies.addAllCookies(cookies));
            /* 提取总页数 */
            int pages = DocumentUtil.extractPages(clickQueryResult);
            log.info("{} {} 年总共{}页", region, year, pages);
            for (int i = 1; i <= pages; i++) {
                try {
                    log.debug("开始爬取 {} {} 年,第 {} 页...", region, year, i);
                    /* 正式开始查询 */
                    String result = httpClient.queryList(listUrl, i, body,
                        (forestRequest, forestCookies) -> forestCookies.addAllCookies(cookies));
                    /* 解析html中的补贴数据 */

                    List<SubsidyInfo> subsidyInfos = DocumentUtil.htmlToList(result, region);
                    /* 解析的数据加入列表 */
                    list.addAll(subsidyInfos);
                    if (i % 30 == 0) {
                        EasyExcel.write(FileUtil.getCanonicalPath(mkdir) + "/补贴车辆-" + i + ".xlsx", SubsidyInfo.class)
                            .sheet().doWrite(list);
                        list.clear();
                        ThreadUtil.safeSleep(8 * 1000);
                    } else if (StrUtil.containsAny(region,"山西","天津")) {
                        ThreadUtil.safeSleep(10 * 1000);
                    } else {
                        ThreadUtil.safeSleep(5000);
                    }
                } catch (ForestNetworkException e) {
                    i--;
                    log.warn("{} {} 年,第 {} 页 网络 错误,休眠60s", region, year, i);
                    ThreadUtil.safeSleep(60 * 1000);
                } catch (Exception e) {
                    i--;
                    log.warn("{} {} 年,第 {} 页出错,休眠3min", region, year, i);
                    ThreadUtil.safeSleep(3 * 60 * 1000);
                }
            }
            log.warn("{} {} 年已完成.", region, year);
            if (CollectionUtil.isNotEmpty(list)) {
                EasyExcel.write(FileUtil.getCanonicalPath(mkdir) + "/补贴车辆-" + pages + ".xlsx", SubsidyInfo.class).sheet()
                    .doWrite(list);
                list.clear();
            }
            // 一年结束 停 5min
            ThreadUtil.safeSleep(5 * 60 * 1000);
        }
        httpClient.sendWXMsg(region + " over", "");
    }


    /**
     * 执行获取 企业供货 信息
     * @param area 地区
     * @param subsidyHttpClient HTTP 客户端
     * @param dir 保存目录
     * @param url 查询地址
     */
    public static void getFhInfo(String area, SubsidyHttpClient subsidyHttpClient, File dir, String url) {
        List<FhInfo> fhInfos = new ArrayList<>();
        File mkdir = FileUtil.mkdir(FileUtil.getAbsolutePath(dir) + "/" + area);
        com.wdnj.xxb.subsidy.entity.factoryFhInfo.RequestBody requestBody = new com.wdnj.xxb.subsidy.entity.factoryFhInfo.RequestBody();
        requestBody.setIsFaH(1);
        requestBody.setPageSize(50);
        log.info("{} 开始处理...", area);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String result = subsidyHttpClient.queryFhInfo(url, 1, requestBody);
        int page = DocumentUtil.getFhPages(result);
        log.info("{} 共 {} 页", area, page);

        for (int i = 1; i <= page; i++) {
            try {
                String pageResult = subsidyHttpClient.queryFhInfo(url, i, requestBody);
                List<FhInfo> list = DocumentUtil.getFhResult(pageResult);
                log.info("{} 第{}页已完成", area, i);
                fhInfos.addAll(list);

                if (i % 30 == 0) {
                    EasyExcel.write(FileUtil.getCanonicalPath(mkdir) + "/企业发货-" + i + ".xlsx", FhInfo.class)
                        .sheet().doWrite(fhInfos);
                    fhInfos.clear();
                }
                //ThreadUtil.safeSleep(1000);
            } catch (Exception e) {
                i--;
                log.warn("{} 第 {} 页出错,休眠3min", area, i);
                ThreadUtil.safeSleep(3 * 60 * 1000);
            }
        }
        EasyExcel.write(FileUtil.getCanonicalPath(mkdir) + "/企业发货-" + page + ".xlsx", FhInfo.class)
            .sheet().doWrite(fhInfos);
        fhInfos.clear();
        stopWatch.stop();
        long taskTimeMillis = stopWatch.getLastTaskTimeMillis();
        log.warn("{} 企业供货完成,共耗时 {}ms", area, taskTimeMillis);
        subsidyHttpClient.sendWXMsg(area + " 企业发货已完成", "共耗时 " + taskTimeMillis);

    }
}
