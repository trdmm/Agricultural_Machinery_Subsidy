package com.wdnj.xxb.subsidy.entity;

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
    @ExcelProperty("序号")
    private String id;

    @ExcelProperty("县")
    private String county;

    @ExcelProperty("所在乡(镇)")
    private String town;

    @ExcelProperty("所在村组")
    private String village;

    @ExcelProperty("购机者姓名")
    private String purchaserName;

    @ExcelProperty("机具品目")
    private String machineItem;

    @ExcelProperty("生产厂家")
    private String factory;

    @ExcelProperty("产品名称")
    private String productName;

    @ExcelProperty("购买机型")
    private String purchaseModel;

    @ExcelProperty("购买数量(台)")
    private String quantity;

    @ExcelProperty("经销商")
    private String dealerName;

    @ExcelProperty("购机日期")
    private String purchaseDate;

    @ExcelProperty("单台销售价格(元)")
    private String sellPrice;

    @ExcelProperty("单台补贴额(元)")
    private String perSubsidyAmount;

    @ExcelProperty("总补贴额(元)")
    private String subsidyAmounts;

    @ExcelProperty("出厂编号")
    private String serialNumber;

    @ExcelProperty("状态")
    private String state;
}
