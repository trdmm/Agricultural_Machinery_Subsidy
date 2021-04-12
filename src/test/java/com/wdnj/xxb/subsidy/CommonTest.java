package com.wdnj.xxb.subsidy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

import cn.hutool.core.util.ArrayUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 描述:...<br>
 *
 * @version 1.0.0
 * @project: Subsidy
 * @file: CommonTest
 * @author: Mingming Huang
 * @since: 2021/3/3 22:42
 */
@Slf4j
public class CommonTest {
    //@Test
    public void test1() {
        try {
            int a = 5 / 1;
        } catch (Exception e) {
            log.error("计算出错 {},{},{}", 1, 2, 3, e);
        }
    }

    //@Test
    public void test2() {
        ThreadPoolExecutor pool = ThreadUtil.newExecutor(5, 10);
        for (int i = 0; i < 10; i++) {
            pool.execute(this::fuck);
        }
    }

    private void fuck() {
        log.info("进来啦...");
        ThreadUtil.sleep(10 * 1000);
    }

    //@Test
    public void test3() {
        try {
            float cnm = cnm();
            log.info("结果={}", cnm);
        } catch (Exception e) {
            log.error("ERROR---", e);
        }
    }

    private float cnm() throws Exception {
        int i = 0;
        try {
            i = 5 / 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    //@Test
    public void test4() {
        StopWatch stopWatch = new StopWatch();
        // stopWatch.setKeepTaskList(false);
        for (int i = 0; i < 500000; i++) {
            stopWatch.start();
            // ThreadUtil.sleep(RandomUtil.randomInt(500,1500));
            ThreadUtil.sleep(1);

            stopWatch.stop();
            stopWatch.setKeepTaskList(false);
            log.info("耗时={}ms", stopWatch.getLastTaskTimeMillis());

        }
        log.info("总耗时={}ms", stopWatch.getTotalTimeMillis());

        ThreadUtil.sleep(30 * 1000);
    }

    //@Test
    public void test5() {
        try {
            docToHtml();
        } catch (NullPointerException e) {
            log.info("出现异常啦");
        }

        // Element tableBody = document.getElementById("list-pub");
        // Elements trs = tableBody.select("tr");
    }

    private void docToHtml() throws NullPointerException {
        Document document = Jsoup.parse("https://www/baidu.com");
        Element tableBody = document.getElementById("list-pub");
        Elements trs = tableBody.select("tr");
    }

    //@Test
    public void test6() {
        ThreadPoolExecutor pool =
            ExecutorBuilder.create().setCorePoolSize(5).setMaxPoolSize(5).setAllowCoreThreadTimeOut(true).build();


        //ThreadPoolExecutor pool = ThreadUtil.newExecutor(5, 5);
        CompletableFuture<Void> future0 = CompletableFuture.runAsync(() -> test7(0),pool);
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> test7(1),pool);
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> test7(2),pool);
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> test7(3),pool);
        CompletableFuture<Void> future4 = CompletableFuture.runAsync(() -> test7(4),pool);
        List<CompletableFuture<Void>> list = new ArrayList<>();
        list.add(future0);
        list.add(future1);
        list.add(future2);
        list.add(future3);
        list.add(future4);
        CompletableFuture<Void> allOf = CompletableFuture.allOf(list.toArray(new CompletableFuture[0]));
        log.info("任务已提交");
        allOf.join();
        log.info("任务已完成");
    }

    private void test7(int i) {
        log.warn("第{}次任务,准备睡眠", i);
        ThreadUtil.sleep(10 * 1000);
        log.info("第{}次任务睡眠完成", i);
    }

    @Test
    public void test8() {
        int[] years = {2018, 2019, 2020};
        years = ArrayUtil.reverse(years);
        for (int i1 : years) {
            log.info("年份:{}",i1);
        }
    }
}
