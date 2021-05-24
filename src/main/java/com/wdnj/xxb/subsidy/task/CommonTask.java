package com.wdnj.xxb.subsidy.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wdnj.xxb.subsidy.task.fhrunnable.GH_FuJianRunnable;
import com.wdnj.xxb.subsidy.task.fhrunnable.GH_HubeiRunnable;
import com.wdnj.xxb.subsidy.task.fhrunnable.GH_ZheJiangRunnable;
import com.wdnj.xxb.subsidy.task.runnable.*;
import com.wdnj.xxb.subsidy.util.SubsidyHttpClient;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ExecutorBuilder;
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

    @Resource
    private SubsidyHttpClient subsidyHttpClient;

    // @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    @Scheduled(cron = "0 0 21 ? * FRI")
    public void getSubsidy() {
        ThreadPoolExecutor pool =
            ExecutorBuilder.create().setCorePoolSize(38).setMaxPoolSize(40).setAllowCoreThreadTimeOut(true).build();

        String rootDirPath = FileUtil.getWebRoot().getPath();
        File dir = FileUtil.mkdir(rootDirPath + "/Excel/" + DateUtil.today() + "/补贴查询");
        log.info("文件夹已创建,开始处理");
        List<CompletableFuture<Void>> list = new ArrayList<>();

        CompletableFuture<Void> bjFuture = CompletableFuture.runAsync(new BJRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> tjFuture = CompletableFuture.runAsync(new TJRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> heBeiFuture =
            CompletableFuture.runAsync(new HeBeiRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> shanXi1Future =
            CompletableFuture.runAsync(new ShanXi1Runnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> jiLinFuture =
            CompletableFuture.runAsync(new JiLinRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> liaoNingFuture =
            CompletableFuture.runAsync(new LiaoNingRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> heNanFuture =
            CompletableFuture.runAsync(new HeNanRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> huBeiFuture =
            CompletableFuture.runAsync(new HuBeiRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> huNanFuture =
            CompletableFuture.runAsync(new HuNanRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> shangHaiFuture =
            CompletableFuture.runAsync(new ShangHaiRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> jiangSuFuture =
            CompletableFuture.runAsync(new JiangSuRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> zheJiangFuture =
            CompletableFuture.runAsync(new ZheJiangRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> anHuiFuture =
            CompletableFuture.runAsync(new AnHuiRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> jiangXiFuture =
            CompletableFuture.runAsync(new JiangXiRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> fuJianFuture =
            CompletableFuture.runAsync(new FuJianRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> shanDongFuture =
            CompletableFuture.runAsync(new ShanDongRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> chongQingFuture =
            CompletableFuture.runAsync(new ChongQingRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> yunNanFuture =
            CompletableFuture.runAsync(new YunNanRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> siChuanFuture =
            CompletableFuture.runAsync(new SiChuanRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> guiZhouFuture =
            CompletableFuture.runAsync(new GuiZhouRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> guangDongFuture =
            CompletableFuture.runAsync(new GuangDongRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> guangXiFuture =
            CompletableFuture.runAsync(new GuangXiRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> xinJiangFuture =
            CompletableFuture.runAsync(new XinJiangRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> haiNanFuture =
            CompletableFuture.runAsync(new HaiNanRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> shanXi3Future =
            CompletableFuture.runAsync(new ShanXi3Runnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> ganSuFuture =
            CompletableFuture.runAsync(new GanSuRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> ningXiaFuture =
            CompletableFuture.runAsync(new NingXiaRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> qingHaiFuture =
            CompletableFuture.runAsync(new QingHaiRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> xiZangFuture =
            CompletableFuture.runAsync(new XiZangRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> daLianFuture =
            CompletableFuture.runAsync(new DaLianRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> qingDaoFuture =
            CompletableFuture.runAsync(new QingDaoRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> ningBoFuture =
            CompletableFuture.runAsync(new NingBoRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> xiaMenFuture =
            CompletableFuture.runAsync(new XiaMenRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> heiLongJiangFuture =
            CompletableFuture.runAsync(new HeiLongJiangRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> neiMengGuFuture =
            CompletableFuture.runAsync(new NeiMengGuRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> guangDongNongKenFuture =
            CompletableFuture.runAsync(new GuangDongNongKenRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> heiLongJiangNongKenFuture =
            CompletableFuture.runAsync(new HeiLongJiangNongKenRunnable(subsidyHttpClient, dir), pool);
        CompletableFuture<Void> xinJiangJianSheBingTuanFuture =
            CompletableFuture.runAsync(new XinJiangJianSheBingTuanRunnable(subsidyHttpClient, dir), pool);

        list.add(bjFuture);
        list.add(tjFuture);
        list.add(heBeiFuture);
        list.add(shanXi1Future);
        list.add(jiLinFuture);
        list.add(liaoNingFuture);
        list.add(heNanFuture);
        list.add(huBeiFuture);
        list.add(huNanFuture);
        list.add(shangHaiFuture);
        list.add(jiangSuFuture);
        list.add(zheJiangFuture);
        list.add(anHuiFuture);
        list.add(jiangXiFuture);
        list.add(fuJianFuture);
        list.add(shanDongFuture);
        list.add(chongQingFuture);
        list.add(yunNanFuture);
        list.add(siChuanFuture);
        list.add(guiZhouFuture);
        list.add(guangDongFuture);
        list.add(guangXiFuture);
        list.add(xinJiangFuture);
        list.add(haiNanFuture);
        list.add(shanXi3Future);
        list.add(ganSuFuture);
        list.add(ningXiaFuture);
        list.add(qingHaiFuture);
        list.add(xiZangFuture);
        list.add(daLianFuture);
        list.add(qingDaoFuture);
        list.add(ningBoFuture);
        list.add(xiaMenFuture);
        list.add(heiLongJiangFuture);
        list.add(neiMengGuFuture);
        list.add(guangDongNongKenFuture);
        list.add(heiLongJiangNongKenFuture);
        list.add(xinJiangJianSheBingTuanFuture);

        CompletableFuture<Void> allOf = CompletableFuture.allOf(list.toArray(new CompletableFuture[0]));
        log.info("补贴爬取任务已提交,坐等~~~");
        allOf.join();
        log.info("补贴爬取任务已完成.");
    }

    //@Scheduled(fixedRate = 7 * 24 * 60 * 60 * 1000)
    @Scheduled(cron = "0 0 20 ? * SAT")
    public void getQiYeGongHuo() {
        String rootDirPath = FileUtil.getWebRoot().getPath();
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
