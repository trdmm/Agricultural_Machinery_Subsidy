package com.wdnj.xxb.subsidy;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

import cn.hutool.core.collection.CollectionUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.wdnj.xxb.subsidy.domain.CityInfo;
import com.wdnj.xxb.subsidy.excel.CityInfoListener;
import com.wdnj.xxb.subsidy.util.ConstantUtil;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
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

    /**
     * 获取各省补贴公示页的区域
     */
    //@Test
    public void getAllCity() throws IOException {
        // Document document = Jsoup
        // .connect("http://202.102.17.134:85/pub/gongshi")
        // .timeout(300 * 1000)
        // .get();
        Document document = Jsoup.parse(FileUtil.file("D:\\Personal\\Desktop\\补贴数据\\公示页.html"), "UTF-8");
        // 省下的城市
        Element citiesTree = document.getElementById("dialog-confirmqy").getElementById("tree");
        Elements cities = citiesTree.children();
        cities.forEach(element -> {
            // 城市名
            String cityName = element.children().get(1).text();
            Console.log("===> {}", cityName);
            // 区县集合
            Elements counties = element.children().get(2).children();
            // counties.forEach(county -> {
            // // 区县名
            // String countyName = county.child(1).text();
            // Console.log("===> {}", countyName);
            // });
            counties.forEach(county -> {
                String countyName = county.select("ul>li>a").text();
                Console.log("===>   {}", countyName);
            });
        });
    }

    //@Test
    public void getAllCity1() throws IOException {
        Document document = Jsoup.parse(FileUtil.file("D:\\Personal\\Desktop\\补贴数据\\公示页.html"), "UTF-8");
        // 省下的城市
        Element citiesTree = document.getElementById("dialog-confirmqy").getElementById("tree");
        Elements cities = citiesTree.children();
        cities.forEach(element -> {
            // 城市名
            String cityName = element.children().get(1).text();
            Console.log("===>  {}", cityName);
            // 区县集合
            Elements counties = element.children().get(2).children();
            counties.forEach(county -> {
                Elements children = county.children();
                switch (children.size()) {
                    case 1:
                        Console.log("===>     {}", children.get(0).text());
                        break;
                    case 3:
                        Console.log("===>     {}", children.get(1).text());
                        break;
                    default:
                        break;
                }
            });
        });
    }

    //@Test
    public void getProvinceCity() throws IOException {
        Field[] fields = ReflectUtil.getFields(ConstantUtil.class);
        Map<String, Map<String, String>> allCities = new HashMap<>();
        for (Field url : fields) {
            if (url.getName().contains("PUBLIC_URL")) {
                String btUrl = (String)ReflectUtil.getStaticFieldValue(url);
                Document document = Jsoup.connect(btUrl).timeout(5 * 60 * 1000).get();
                Map<String, String> map = getCities(document);
                // 省,Map<区,市>
                allCities.put(url.getName().split("_")[0], map);
                Console.log("{} 省获取完成.", url.getName().split("_")[0]);
            }
        }
        ExcelWriter writer = null;
        try {
            writer = EasyExcel.write("D:\\Personal\\Desktop\\补贴数据\\各省市区县对应关系.xlsx", CityInfo.class).build();

            Set<Map.Entry<String, Map<String, String>>> provinces = allCities.entrySet();
            for (Map.Entry<String, Map<String, String>> entry : provinces) {
                // Console.log("开始写 {}",entry.getKey());
                // WriteSheet sheet = EasyExcel.writerSheet(entry.getKey()).build();
                // List<CityInfo> cityInfos = new ArrayList<>();
                // Map<String, String> value = entry.getValue();
                // Set<Map.Entry<String, String>> cities = value.entrySet();
                // for (Map.Entry<String, String> city : cities) {
                // CityInfo cityInfo = new CityInfo();
                // cityInfo.setCounty(city.getValue());
                // cityInfo.setCity(city.getKey());
                // cityInfos.add(cityInfo);
                // }
                // writer.write(cityInfos,sheet);
                Console.log("开始写 {}", entry.getKey());
                WriteSheet sheet = EasyExcel.writerSheet(entry.getKey()).build();
                List<CityInfo> cityInfos = new ArrayList<>();
                Map<String, String> province = entry.getValue();
                Set<Map.Entry<String, String>> cities = province.entrySet();
                for (Map.Entry<String, String> city : cities) {
                    CityInfo cityInfo = new CityInfo();
                    cityInfo.setCounty(city.getKey());
                    cityInfo.setCity(city.getValue());
                    cityInfos.add(cityInfo);
                }
                writer.write(cityInfos, sheet);
            }
        } finally {
            if (writer != null) {
                writer.finish();
            }
        }

        Console.log("处理完成");
    }

    private Map<String, String> getCities(Document document) {
        Element citiesTree = document.getElementById("dialog-confirmqy").getElementById("tree");
        Elements cities = citiesTree.children();
        Map<String, String> map = new HashMap<>();
        cities.forEach(element -> {
            // 城市名
            String cityName = element.children().get(1).text();
            // 区县集合
            Elements counties = element.children().get(2).children();
            counties.forEach(county -> {
                Elements children = county.children();
                switch (children.size()) {
                    case 1:
                        // Console.log("===> {}", children.get(0).text());
                        map.put(children.get(0).text(), cityName);
                        break;
                    case 3:
                        // Console.log("===> {}", children.get(1).text());
                        map.put(children.get(1).text(), cityName);
                        break;
                    default:
                        break;
                }
            });
        });
        return map;
    }

    // @Test
    public void test1() {
        try {
            int a = 5 / 1;
        } catch (Exception e) {
            log.error("计算出错 {},{},{}", 1, 2, 3, e);
        }
    }

    // @Test
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

    // @Test
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

    // @Test
    public void test4() {
        StopWatch stopWatch = new StopWatch();
        // stopWatch.setKeepTaskList(false);
        for (int i = 0; i < 500000; i++) {
            stopWatch.start();
            // ThreadUtil.sleep(RandomUtil.randomInt(500,1500));
            //ThreadUtil.sleep(1);

            stopWatch.stop();
            stopWatch.setKeepTaskList(false);
            log.info("耗时={}ms", stopWatch.getLastTaskTimeMillis());

        }
        log.info("总耗时={}ms", stopWatch.getTotalTimeMillis());

        ThreadUtil.sleep(30 * 1000);
    }

    // @Test
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

    // @Test
    public void test6() {
        ThreadPoolExecutor pool =
            ExecutorBuilder.create().setCorePoolSize(5).setMaxPoolSize(5).setAllowCoreThreadTimeOut(true).build();

        // ThreadPoolExecutor pool = ThreadUtil.newExecutor(5, 5);
        CompletableFuture<Void> future0 = CompletableFuture.runAsync(() -> test7(0), pool);
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> test7(1), pool);
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> test7(2), pool);
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> test7(3), pool);
        CompletableFuture<Void> future4 = CompletableFuture.runAsync(() -> test7(4), pool);
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

    //@Test
    public void test8() {
        int[] years = {2018, 2019, 2020};
        years = ArrayUtil.reverse(years);
        for (int i1 : years) {
            log.info("年份:{}", i1);
        }
    }

    //@Test
    public void test9() {
        Map<String, Map<String, String>> map = new HashMap<>();

        EasyExcel.read("E:\\Projects\\项目文件\\补贴数据爬取\\经销商平台写入\\各省市区县对应关系.xlsx", CityInfo.class, new CityInfoListener(map))
            .doReadAll();
    }

    //@Test
    public void test10() {
        String county = "海安县";
        Console.log(county.substring(0, county.length() - 1) + "市");
        Console.log(county.substring(0, county.length() - 1) + "县");
        Console.log(county.substring(0, county.length() - 1) + "区");

        Map<String, Map<String, String>> map =new HashMap<>();
        HashMap<String, String> map1 = new HashMap<>();
        map1.put("fuck","cnm");
        map.put("lueluelue",map1);
        Map<String, String> xxx = map.get("xxx");

        Console.log("xxx存在?", CollectionUtil.isNotEmpty(xxx));
    }

}
