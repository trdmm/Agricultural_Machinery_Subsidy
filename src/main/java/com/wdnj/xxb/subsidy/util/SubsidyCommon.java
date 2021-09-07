package com.wdnj.xxb.subsidy.util;

import java.io.File;
import java.util.*;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.dtflys.forest.exceptions.ForestNetworkException;
import com.dtflys.forest.http.ForestCookie;
import com.dtflys.forest.http.HttpStatus;
import com.wdnj.xxb.subsidy.entity.ding_talk.DingTalkMsg;
import com.wdnj.xxb.subsidy.entity.ding_talk.Text;
import com.wdnj.xxb.subsidy.entity.factoryFhInfo.FhInfo;
import com.wdnj.xxb.subsidy.entity.subsidyInfo.new_app.RequestBodyNewer;
import com.wdnj.xxb.subsidy.entity.subsidyInfo.new_app.SubsidyInfoResponse;
import com.wdnj.xxb.subsidy.entity.subsidyInfo.old_app.RequestBodyOlder;
import com.wdnj.xxb.subsidy.entity.subsidyInfo.SubsidyInfo;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
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

    /** 采用新版本的省 */
    private static final Set<String> newAppProvince = new HashSet<String>(){{
        add("江苏省");
        add("四川省");
    }};

    /** BeanUtil 赋值属性Map */
    private static final Map<String, String> FIELD_MAP = new HashMap<String,String>(){{
        put("id","id");
        put("years","fundingYear");
        put("county","county");
        put("town","town");
        put("village","village");
        put("buyName","purchaserName");
        put("itemName","machineItem");
        put("factoryName","factory");
        put("productName","productName");
        put("machineCode","purchaseModel");
        put("equipmentNo","quantity");
        put("agentName","dealerName");
        put("purchaseDate","purchaseDate");
        put("everySalePrice","sellPrice");
        put("everySubsidy","perCountryAmount");
        put("proEverySubsidy","perProvinceAmount");
        put("","discountSubsidy");
        put("sumSubsidy","subsidyAmounts");
        put("factoryNumber","serialNumber");
        put("status","state");
    }};

    private static final int MAX_PAGE_SIZE = 100;
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

            if (StrUtil.endWith(publicUrl,"gongshi")) {
                // 旧版应用
                oldSubsidy(region, httpClient, dir, year, publicUrl);
            } else if (StrUtil.endWith(publicUrl,"subsidyOpen")) {
                // 新版应用
                newSubsidy(region, httpClient, dir, year, publicUrl);
            } else {
                // 不知道哪个版本
                Text text = new Text(region + "-" + year + "年无法根据爬取地址确定爬取逻辑(沃得)");
                httpClient.sendDingTalkMsg(DingTalkMsg.builder().msgType("text").text(text).build());
                continue;
            }

            // 一年结束 停 5min
            ThreadUtil.safeSleep(5 * 60 * 1000);
        }

        Text text = new Text(region + " over(沃得)");
        httpClient.sendDingTalkMsg(DingTalkMsg.builder().msgType("text").text(text).build());
        log.info("{} 全部完成",region);
    }

    /**
     * 旧版本的补贴网站数据
     * @param region 省份地区
     * @param httpClient http 客户端
     * @param dir 文件保存目录
     * @param year 年份
     * @param publicUrl 公示地址
     */
    private static void oldSubsidy(String region, SubsidyHttpClient httpClient, File dir, int year, String publicUrl) {
        // 点击搜索后地址
        String listUrl = publicUrl.replace("gongshi", "GongShiSearch");

        /* 存放cookie */
        List<ForestCookie> cookies = new ArrayList<>();
        /* 打开公示页 */
        String publishPage = httpClient.openPublishPage(publicUrl,
            (forestRequest, forestCookies) -> cookies.addAll(forestCookies.allCookies()));
        /* 获取公示页面中的token */
        String token = DocumentUtil.extractBodyCookie(publishPage);
        if (token == null){
            // 本年度无数据
            return;
        }

        if (StrUtil.isBlank(token) && !newAppProvince.contains(region)) {
            // 没获取到 token,可能逻辑已改变,发送消息
            Text text = new Text(region + "-" + year + "年获取token失败,请查看是否逻辑已修改(沃得)");
            httpClient.sendDingTalkMsg(DingTalkMsg.builder().msgType("text").text(text).build());
            return;
        }

        /* 存放补贴信息 */
        List<SubsidyInfo> list = new ArrayList<>();
        /* 计数 */
        // AtomicInteger atomicInteger = new AtomicInteger();

        /* 地区年份文件夹 */
        File mkdir = FileUtil.mkdir(FileUtil.getAbsolutePath(dir) + "/" + region + "/" + year);

        /* 表单内容 */
        RequestBodyOlder body = RequestBodyOlder.builder()
            .year(String.valueOf(year))
            .requestVerificationToken(token)
            .build();

        ThreadUtil.sleep(3000);
        /* 点击公示页查询结果 */
        String clickQueryResult = httpClient.clickQuery(listUrl, body,
            (forestRequest, forestCookies) -> forestCookies.addAllCookies(cookies));
        /* 提取总页数 */
        int pages = DocumentUtil.extractPages(clickQueryResult);

        if (pages == 0) {
            // 提取总页数出错
            Text text = new Text(region + "-" + year + "年提取总页数出错(沃得)");
            httpClient.sendDingTalkMsg(DingTalkMsg.builder().msgType("text").text(text).build());
            return;
        }

        log.info("{} {} 年总共{}页", region, year, pages);
        for (int i = 1; i <= pages; i++) {
            try {
                log.debug("开始爬取 {} {} 年,第 {}/{} 页...", region, year, i, pages);
                /* 正式开始查询 */
                String result = httpClient.queryList(listUrl, i, body,
                    (forestRequest, forestCookies) -> forestCookies.addAllCookies(cookies));
                /* 解析html中的补贴数据 */

                List<SubsidyInfo> subsidyInfos = DocumentUtil.htmlToList(result, region, year);
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
                log.error("{} {} 年,第 {} 页 网络 错误,休眠60s", region, year, i);
                i--;
                ThreadUtil.safeSleep(60 * 1000);
            } catch (Exception e) {
                log.error("{} {} 年,第 {} 页出错,休眠3min", region, year, i);
                i--;
                ThreadUtil.safeSleep(3 * 60 * 1000);
            }
        }

        if (CollectionUtil.isNotEmpty(list)) {
            EasyExcel.write(FileUtil.getCanonicalPath(mkdir) + "/补贴车辆-" + pages + ".xlsx", SubsidyInfo.class).sheet()
                .doWrite(list);
            list.clear();
        }

        log.warn("{} {} 年已完成.", region, year);
    }

    /**
     * 新版本的补贴网站数据
     * @param region 省份地区
     * @param httpClient http 客户端
     * @param dir 文件保存目录
     * @param year 年份
     * @param publicUrl 公示地址
     */
    private static void newSubsidy(String region, SubsidyHttpClient httpClient, File dir, int year, String publicUrl) {
        // http://butiexitong.gagogroup.cn:8081/subsidyOpen
        // ->
        // http://butiexitong.gagogroup.cn:8081/api/api/loginSidePage/getPurchaseSubsidyOpenList

        String url = StrUtil.replace(publicUrl, "subsidyOpen", "api/api/loginSidePage/getPurchaseSubsidyOpenList");

        RequestBodyNewer body = new RequestBodyNewer(year);

        SubsidyInfoResponse subsidyInfoTest = httpClient.getSubsidyInfo(url, body, 1, MAX_PAGE_SIZE);
        int pages;
        if (subsidyInfoTest.getCode() == HttpStatus.OK) {
            // 请求成功
            int total = subsidyInfoTest.getTotal();
            pages = NumberUtil.ceilDiv(total, MAX_PAGE_SIZE);
        } else {
            Text text = new Text(region + "-" + year + " 新版补贴数据获取失败(沃得)");
            httpClient.sendDingTalkMsg(DingTalkMsg.builder().msgType("text").text(text).build());
            return;
        }

        /* 地区年份文件夹 */
        File mkdir = FileUtil.mkdir(FileUtil.getAbsolutePath(dir) + "/" + region + "/" + year);

        List<SubsidyInfo> list = new ArrayList<>();

        CopyOptions copyOptions = CopyOptions.create();
        copyOptions.setFieldMapping(FIELD_MAP);

        log.info("{} {} 年总共{}页", region, year, pages);
        for (int i = 1; i <= pages; i++) {
            log.debug("开始爬取新版本 {} {} 年,第 {}/{} 页...", region, year, i, pages);
            SubsidyInfoResponse response = httpClient.getSubsidyInfo(url, body, i, MAX_PAGE_SIZE);

            if (response.getCode() != HttpStatus.OK) {
                // 失败
                log.error("新版本 {} {} 年,第 {} 页出错,休眠3min", region, year, i);
                i--;
                ThreadUtil.safeSleep(3 * 60 * 1000);
                continue;
            }

            response.getRows().forEach(rowsItem -> {
                SubsidyInfo subsidyInfo = BeanUtil.toBean(rowsItem, SubsidyInfo.class, copyOptions);
                list.add(subsidyInfo);
            });

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
        }

        if (CollectionUtil.isNotEmpty(list)) {
            EasyExcel.write(FileUtil.getCanonicalPath(mkdir) + "/补贴车辆-" + pages + ".xlsx", SubsidyInfo.class).sheet()
                .doWrite(list);
            list.clear();
        }

        log.warn("新版本 {} {} 年已完成.", region, year);
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
        String result = null;
        try {
            result = subsidyHttpClient.queryFhInfo(url, 1, requestBody);
        } catch (Exception e) {
            log.error("{} 企业发货: {},出错!",area,url,e);
            Text text = new Text(area + " 企业发货出错(沃得): "+url);
            subsidyHttpClient.sendDingTalkMsg(DingTalkMsg.builder().msgType("text").text(text).build());
            return;
        }
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
                log.error("{} 第 {} 页出错,休眠3min", area, i);
                i--;
                ThreadUtil.safeSleep(3 * 60 * 1000);
            }
        }
        if (CollectionUtil.isNotEmpty(fhInfos)) {
            EasyExcel.write(FileUtil.getCanonicalPath(mkdir) + "/企业发货-" + page + ".xlsx", FhInfo.class)
                .sheet().doWrite(fhInfos);
            fhInfos.clear();
        }

        log.warn("{} 企业供货完成", area);
        Text text = new Text(area + " 企业发货已完成(沃得)");
        subsidyHttpClient.sendDingTalkMsg(DingTalkMsg.builder().msgType("text").text(text).build());
    }
}
