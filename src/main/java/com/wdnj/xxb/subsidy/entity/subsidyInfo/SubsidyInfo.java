package com.wdnj.xxb.subsidy.entity.subsidyInfo;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;

/**
 * 描述:...<br>
 *
 * @version 1.0.0
 * @project: Subsidy
 * @file: SubsidyInfo
 * @author: Mingming Huang
 * @since: 2021/3/3 16:14
 */
@Data
public class SubsidyInfo {
    /**
     * 序号 县 所在乡(镇) 所在村组 购机者姓名 机具品目 生产厂家 产品名称 购买机型 购买数量(台) 经销商 购机日期 单台销售价格(元) 单台补贴额(元) 总补贴额(元) 出厂编号 状态
     */

    /** 序号 */
    @ExcelProperty("序号")
    private String id;

    /** 资金年度 */
    @ExcelProperty("资金年度")
    private int fundingYear;

    /** 县 */
    @ExcelProperty("县")
    private String county;

    /** 所在乡(镇) */
    @ExcelProperty("所在乡(镇)")
    private String town;

    /** 所在村组 */
    @ExcelProperty("所在村组")
    private String village;

    /** 购机者姓名 */
    @ExcelProperty("购机者姓名")
    private String purchaserName;

    /** 机具品目 */
    @ExcelProperty("机具品目")
    private String machineItem;

    /** 生产厂家 */
    @ExcelProperty("生产厂家")
    private String factory;

    /** 产品名称 */
    @ExcelProperty("产品名称")
    private String productName;

    /** 购买机型 */
    @ExcelProperty("购买机型")
    private String purchaseModel;

    /** 购买数量(台) */
    @ExcelProperty("购买数量(台)")
    private String quantity;

    /** 经销商 */
    @ExcelProperty("经销商")
    private String dealerName;

    /** 购机日期 */
    @ExcelProperty("购机日期")
    private String purchaseDate;

    /** 单台销售价格(元) */
    @ExcelProperty("单台销售价格(元)")
    private String sellPrice;

    /** 单台/中央补贴额(元) */
    @ExcelProperty("单台/中央补贴额(元)")
    private String perCountryAmount;

    /** 单台/省级补贴额(元) */
    @ExcelProperty("单台/省级补贴额(元)")
    private String perProvinceAmount;

    /** 贴息补贴额(元) */
    @ExcelProperty("贴息补贴额(元)")
    private String discountSubsidy;

    /** 总补贴额(元) */
    @ExcelProperty("总补贴额(元)")
    private String subsidyAmounts;

    /** 出厂编号 */
    @ExcelProperty("出厂编号")
    private String serialNumber;

    /** 状态 */
    @ExcelProperty("状态")
    private String state;
}
