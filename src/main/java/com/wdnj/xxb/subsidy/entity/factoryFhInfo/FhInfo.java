package com.wdnj.xxb.subsidy.entity.factoryFhInfo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 描述: 企业供货信息<br/>
 *
 * @author Knight
 * @version 1.0.0
 * @since 2021-05-19 15:46
 */
@Data
public class FhInfo {
    /**
     * 生产企业	经销商	产品型号	出厂编号	发动机号	发动机型号	发货单号	发货时间	是否发货	是否已办理补贴	是否封闭	是否配备柴油发动机
     */

    /**
     * 生产企业
     */
    @ExcelProperty("生产企业")
    private String factoryName;
    /**
     * 经销商
     */
    @ExcelProperty("经销商")
    private String jxs;
    /**
     * 产品型号
     */
    @ExcelProperty("产品型号")
    private String cpxh;
    /**
     * 出厂编号
     */
    @ExcelProperty("出厂编号")
    private String ccbh;
    /**
     * 发动机号
     */
    @ExcelProperty("发动机号")
    private String fdjh;
    /**
     * 发动机型号
     */
    @ExcelProperty("发动机型号")
    private String fdjxh;
    /**
     * 发货单号
     */
    @ExcelProperty("发货单号")
    private String fhdh;
    /**
     * 发货时间
     */
    @ExcelProperty("发货时间")
    private String fhsj;
    /**
     * 是否发货
     */
    @ExcelProperty("是否发货")
    private String sffh;
    /**
     * 是否已办理补贴
     */
    @ExcelProperty("是否已办理补贴")
    private String sfyblbt;
    /**
     * 是否封闭
     */
    @ExcelProperty("是否封闭")
    private String sffb;
    /**
     * 是否配备柴油发动机
     */
    @ExcelProperty("是否配备柴油发动机")
    private String sfpbcyfdj;
}
