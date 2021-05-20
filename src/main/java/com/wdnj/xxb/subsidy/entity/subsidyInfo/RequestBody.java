package com.wdnj.xxb.subsidy.entity.subsidyInfo;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Builder;
import lombok.Data;

/**
 * 描述:...<br>
 *
 * @version 1.0.0
 * @project: Subsidy
 * @file: RequestBody
 * @author: Mingming Huang
 * @since: 2021/3/3 18:32
 */
@Data
@Builder
public class RequestBody {
    /**
     * __RequestVerificationToken
     * "r2Ro07U6ab5sGj56VJxE6yiKwi0ODOVUVXJWORLrUO0eE3fIf9xX62i2IWaajJo5hBzMPy8Q8OF5/pzAiTKI02sHuCNCI9PaBOrBV3N4gu03ZAtHBKazqyUtbE/Pt75+p92DnKjNKz9vTXC4bSTzQSjeuKq473UgvDTkwPKVDnM="
     * YearNum "2020" areaName "" AreaCode "" qy "" n "" JiJuLeiXing "" JiJuLeiXingCode "" FactoryName "" BusinessName
     * "" ChuCBH "" StartGJRiQi "" EndGJRiQi "" StateValue "" StateName ""
     */
    @JSONField(name = "__RequestVerificationToken")
    private String requestVerificationToken;
    @JSONField(name = "YearNum")
    private String year;
    @JSONField(name = "JiJuLeiXing")
    private String jiJuLeiXing;
    @JSONField(name = "JiJuLeiXingCode")
    private String jiJuLeiXingCode;
}
