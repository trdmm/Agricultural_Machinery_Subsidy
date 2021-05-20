package com.wdnj.xxb.subsidy.util;

import java.util.ArrayList;
import java.util.List;

import com.wdnj.xxb.subsidy.entity.factoryFhInfo.FhInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.wdnj.xxb.subsidy.entity.subsidyInfo.SubsidyInfo;

import cn.hutool.core.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 描述: 正式查询列表<br>
 *
 * @version 1.0.0
 * @project: Subsidy
 * @file: DocumentUtil
 * @author: Mingming Huang
 * @since: 2021/3/3 16:09
 */
@Slf4j
public class DocumentUtil {
    /**
     * 结果页转List<br/>
     * 江西地区有列 "贴息补贴"
     *
     * @param html
     *            需要解析的html页面(String)
     * @param area
     *            地区
     * @throws NullPointerException
     *             请求过快返回错误页面,无法解析
     * @return 结果集合
     */
    public static List<SubsidyInfo> htmlToList(String html, String area) throws NullPointerException {
        List<SubsidyInfo> list = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Element tableBody = document.getElementById("list-pub");
        Elements trs = tableBody.select("tr");
        if ("江西".equals(area)) {
            trs.forEach(tr -> {
                Elements tds = tr.select("td");
                SubsidyInfo subsidyInfo = new SubsidyInfo();
                for (int i = 0, length = tds.size(); i < length; i++) {
                    String attr = tds.get(i).text();
                    switch (i) {
                        case 0:
                            subsidyInfo.setId(attr);
                            break;
                        case 1:
                            subsidyInfo.setCounty(attr);
                            break;
                        case 2:
                            subsidyInfo.setTown(attr);
                            break;
                        case 3:
                            subsidyInfo.setVillage(attr);
                            break;
                        case 4:
                            subsidyInfo.setPurchaserName(attr);
                            break;
                        case 5:
                            subsidyInfo.setMachineItem(attr);
                            break;
                        case 6:
                            subsidyInfo.setFactory(attr);
                            break;
                        case 7:
                            subsidyInfo.setProductName(attr);
                            break;
                        case 8:
                            subsidyInfo.setPurchaseModel(attr);
                            break;
                        case 9:
                            subsidyInfo.setQuantity(attr);
                            break;
                        case 10:
                            subsidyInfo.setDealerName(attr);
                            break;
                        case 11:
                            subsidyInfo.setPurchaseDate(attr);
                            break;
                        case 12:
                            subsidyInfo.setSellPrice(attr);
                            break;
                        case 13:
                            subsidyInfo.setPerSubsidyAmount(attr);
                            break;
                        case 14:
                            subsidyInfo.setDiscountSubsidy(attr);
                            break;
                        case 15:
                            subsidyInfo.setSubsidyAmounts(attr);
                            break;
                        case 16:
                            subsidyInfo.setSerialNumber(attr);
                            break;
                        case 17:
                            subsidyInfo.setState(attr);
                            break;
                        default:
                            break;
                    }
                }
                list.add(subsidyInfo);
            });
        } else {
            trs.forEach(tr -> {
                Elements tds = tr.select("td");
                SubsidyInfo subsidyInfo = new SubsidyInfo();
                for (int i = 0, length = tds.size(); i < length; i++) {
                    String attr = tds.get(i).text();
                    switch (i) {
                        case 0:
                            subsidyInfo.setId(attr);
                            break;
                        case 1:
                            subsidyInfo.setCounty(attr);
                            break;
                        case 2:
                            subsidyInfo.setTown(attr);
                            break;
                        case 3:
                            subsidyInfo.setVillage(attr);
                            break;
                        case 4:
                            subsidyInfo.setPurchaserName(attr);
                            break;
                        case 5:
                            subsidyInfo.setMachineItem(attr);
                            break;
                        case 6:
                            subsidyInfo.setFactory(attr);
                            break;
                        case 7:
                            subsidyInfo.setProductName(attr);
                            break;
                        case 8:
                            subsidyInfo.setPurchaseModel(attr);
                            break;
                        case 9:
                            subsidyInfo.setQuantity(attr);
                            break;
                        case 10:
                            subsidyInfo.setDealerName(attr);
                            break;
                        case 11:
                            subsidyInfo.setPurchaseDate(attr);
                            break;
                        case 12:
                            subsidyInfo.setSellPrice(attr);
                            break;
                        case 13:
                            subsidyInfo.setPerSubsidyAmount(attr);
                            break;
                        case 14:
                            subsidyInfo.setSubsidyAmounts(attr);
                            break;
                        case 15:
                            subsidyInfo.setSerialNumber(attr);
                            break;
                        case 16:
                            subsidyInfo.setState(attr);
                            break;
                        default:
                            break;
                    }
                }
                list.add(subsidyInfo);
            });
        }
        return list;
    }

    /**
     * 提取页面的cookie -- ASP.SessionId
     * 
     * @param html
     *            存放cookie的页面
     * @return 有效cookie
     */
    public static String extractAspCookie(String html) {
        Document document = Jsoup.parse(html);
        Elements inputs = document.getElementsByTag("input");
        Element element = inputs.select("[name=__RequestVerificationToken]").get(0);
        return element.val();
    }

    /**
     * 提取公示页的token -- body内容
     *
     * @param html
     *            存放token的页面
     * @return 页数和有效cookie
     */
    public static String extractBodyCookie(String html) {
        Document document = Jsoup.parse(html);
        Element form = document.getElementsByClass("ser").get(0).select("form").get(0);
        Element cookieInput = form.getElementsByAttributeValue("name", "__RequestVerificationToken").get(0);

        // 页数
        Element lastPage = document.getElementsByClass("pagerItem").get(0).select("a").last();
        String target = lastPage.attr("href");
        int pages = NumberUtil.parseInt(target.split("=")[1]);

        return cookieInput.val();
    }

    /**
     * 提取页数 -- body内容
     *
     * @param html
     *            存放页码的页面
     * @return 页数和有效cookie
     */
    public static int extractPages(String html) {
        Document document = Jsoup.parse(html);

        // 页数
        Element lastPage = document.getElementsByClass("pagerItem").get(0).select("a").last();
        String target = lastPage.attr("href");

        return NumberUtil.parseInt(target.split("=")[1]);
    }

    /**
     * 获取企业发货信息 页数
     * @param result HTML页面
     * @return 当前pageSize的页数
     */
    public static int getFhPages(String result) {
        Document document = Jsoup.parse(result);
        //File file = FileUtil.file("C:\\Users\\Knight\\Desktop\\fhinfo.html");
        //Document document = Jsoup.parse(file, StandardCharsets.UTF_8.name());
        String pageInfo = document.getElementsByClass("dcou").get(0).text();
        String[] split = pageInfo.split("/");

        return NumberUtil.parseInt(split[1]);
    }

    public static List<FhInfo> getFhResult(String result) {
        Document document = Jsoup.parse(result);
        List<FhInfo> list = new ArrayList<>();
        Element tbody = document.getElementsByTag("tbody").get(1);
        Elements trs = tbody.select("tr");
        trs.forEach(element -> {
            Elements tds = element.select("td");
            FhInfo fhInfo = new FhInfo();
            for (int i = 0,length=tds.size(); i < length; i++) {
                String attr = tds.get(i).text();
                switch (i){
                    case 0:
                        fhInfo.setFactoryName(attr);
                        break;
                    case 1:
                        fhInfo.setJxs(attr);
                        break;
                    case 2:
                        fhInfo.setCpxh(attr);
                        break;
                    case 3:
                        fhInfo.setCcbh(attr);
                        break;
                    case 4:
                        fhInfo.setFdjh(attr);
                        break;
                    case 5:
                        fhInfo.setFdjxh(attr);
                        break;
                    case 6:
                        fhInfo.setFhdh(attr);
                        break;
                    case 7:
                        fhInfo.setFhsj(attr);
                        break;
                    case 8:
                        fhInfo.setSffh(attr);
                        break;
                    case 9:
                        fhInfo.setSfyblbt(attr);
                        break;
                    case 10:
                        fhInfo.setSffb(attr);
                        break;
                    case 11:
                        fhInfo.setSfpbcyfdj(attr);
                        break;
                    default:
                        break;
                }
            }
            list.add(fhInfo);
        });
        return list;
    }
}
