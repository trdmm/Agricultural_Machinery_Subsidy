package com.wdnj.xxb.subsidy.task;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.Resource;

import com.wdnj.xxb.subsidy.entity.ding_talk.DingTalkMsg;
import com.wdnj.xxb.subsidy.entity.ding_talk.Text;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.wdnj.xxb.subsidy.entity.subsidyInfo.ProvinceInfo;
import com.wdnj.xxb.subsidy.entity.subsidyInfo.ProvinceYearInfo;
import com.wdnj.xxb.subsidy.entity.subsidyInfo.YearInfo;
import com.wdnj.xxb.subsidy.task.fhrunnable.GH_FuJianRunnable;
import com.wdnj.xxb.subsidy.task.fhrunnable.GH_HubeiRunnable;
import com.wdnj.xxb.subsidy.task.fhrunnable.GH_ZheJiangRunnable;
import com.wdnj.xxb.subsidy.util.SubsidyCommon;
import com.wdnj.xxb.subsidy.util.SubsidyHttpClient;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 描述:...<br>
 *
 * @version 1.0.0
 * @project: Subsidy
 * @file: CommonTask
 * @author: Mingming Huang
 * @since: 2021/3/3 17:20
 */
@Component
@EnableScheduling
@Slf4j
public class CommonTask {

    // 打正式包用,本地测试时会被覆盖
    private final String rootDirPath = FileUtil.getWebRoot().getParentFile().getPath();

    @Resource
    private SubsidyHttpClient subsidyHttpClient;


    // @Scheduled(fixedRate = 7*24*60*60*1000)
    @Scheduled(cron = "0 0 19 ? * FRI")
    public void getSubsidyInfo(){
        // 一个地区多个年份,一个年份一个地址
        //             地区        年份    地址
        Multimap<String, YearInfo> map = ArrayListMultimap.create();

        // 根目录(存放配置信息)
        // TODO 打包时注释,自动采用类中定义的变量
        // String rootDirPath = FileUtil.getWebRoot().getPath();
        // 2018-2020 & 2021-2023 补贴系统地址 (注: 江苏省需要跳转二级地址  2021-08-14)
        List<String> urls = FileUtil.readUtf8Lines(rootDirPath + "/conf/url");
        // 2018-2020 各年份信息
        String provinceYearInfo = FileUtil.readUtf8String(rootDirPath + "/conf/ProvinceYearInfo.json");

        // 整合各省各年的信息
        for (int j = 0; j < urls.size(); j++) {
            String url = urls.get(j);
            try {
                String result = subsidyHttpClient.getLatestUrl(url);

                int length = result.length();
                int startIndex = 0;
                int endIndex = length-1;
                for (int i = 0; i < length; i++) {

                    if (result.charAt(i) == '['){
                        startIndex = i;
                        break;
                    }
                }

                for (int i = length-1; i >= 0 ; i--) {
                    if (result.charAt(i) == ']'){
                        endIndex = i+1;
                        break;
                    }
                }
                // 各省级行政区划对应的地址字符串
                String urlList = result.substring(startIndex, endIndex);
                JSONArray urlArray = JSONUtil.parseArray(urlList);
                // 各省级行政区划对应的地址List
                List<ProvinceInfo> provinceInfos = JSONUtil.toList(urlArray, ProvinceInfo.class);

                // 同一年的不同地区 url 的 map
                Map<String,String> urlMap = new HashMap<String,String>();
                provinceInfos.forEach(provinceInfo -> urlMap.put(provinceInfo.getProvince(),provinceInfo.getUrl()));

                JSONArray yearsArray = JSONUtil.parseArray(provinceYearInfo);
                // 各省级行政区划对应的年份 List (2018-2020)
                List<ProvinceYearInfo> provinceYearInfos = JSONUtil.toList(yearsArray, ProvinceYearInfo.class);

                int finalJ = j;
                provinceYearInfos.forEach(provinceYearInfo1 -> {
                    String province = provinceYearInfo1.getProvince();
                    String yearUrl = urlMap.get(province);

                    if (StrUtil.isBlank(yearUrl)) {
                        return;
                    }

                    if (finalJ == 1){
                        // 2018-2020 年
                        int[] years = ArrayUtil.reverse(provinceYearInfo1.getYears());
                        for (int year : years) {
                            YearInfo yearInfo = new YearInfo();
                            yearInfo.setYear(year);
                            yearInfo.setUrl(yearUrl);

                            map.put(province, yearInfo);
                        }
                    } else {
                        YearInfo yearInfo = new YearInfo();
                        yearInfo.setYear(2021);
                        yearInfo.setUrl(yearUrl);
                        map.put(province,yearInfo);
                    }

                });
            }catch (Exception e){
                log.error("补贴数据获取最新公示地址错误",e);
                Text text = new Text("补贴数据获取最新公示地址错误(沃得): " + DateUtil.now());
                subsidyHttpClient.sendDingTalkMsg(DingTalkMsg.builder().msgType("text").text(text).build());
            }
        }

        log.info("开始爬取数据");
        ThreadPoolExecutor pool = ExecutorBuilder.create()
            .setCorePoolSize(38)
            .setMaxPoolSize(38)
            .setAllowCoreThreadTimeOut(true)
            .build();

        File dir = FileUtil.mkdir(rootDirPath + "/Excel/" + DateUtil.today() + "/补贴查询");
        log.info("文件夹已创建,开始处理");
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        Set<String> provinceSet = map.keySet();

        provinceSet.forEach(province -> {
            Collection<YearInfo> yearInfos = map.get(province);
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                SubsidyCommon.getSubsidy(province,subsidyHttpClient,yearInfos,dir);
            }, pool);
            futures.add(future);
        });

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.join();

        log.info("补贴数据爬取完成");
        Text text = new Text("补贴数据爬取完成(沃得): " + DateUtil.now());
        subsidyHttpClient.sendDingTalkMsg(DingTalkMsg.builder().msgType("text").text(text).build());
    }

    //@Scheduled(fixedRate = 7 * 24 * 60 * 60 * 1000)
    // @Scheduled(cron = "0 0 20 ? * SAT")
    public void getQiYeGongHuo() {
        //String rootDirPath = FileUtil.getWebRoot().getPath();
        File dir = FileUtil.mkdir(rootDirPath + "/Excel/" + DateUtil.today() + "/企业供货");
        log.info("文件夹已创建,开始处理");

        CompletableFuture<Void> huBeiFuture =
            CompletableFuture.runAsync(new GH_HubeiRunnable(subsidyHttpClient, dir));
        CompletableFuture<Void> zheJiangFuture =
            CompletableFuture.runAsync(new GH_ZheJiangRunnable(subsidyHttpClient, dir));
        CompletableFuture<Void> fuJianFuture =
            CompletableFuture.runAsync(new GH_FuJianRunnable(subsidyHttpClient, dir));

        CompletableFuture<Void> allOf = CompletableFuture.allOf(huBeiFuture, zheJiangFuture, fuJianFuture);
        log.info("企业供货任务已提交,坐等~~~");
        allOf.join();
        log.info("企业供货任务完成.");
    }
}
