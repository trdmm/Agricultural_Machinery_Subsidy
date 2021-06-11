package com.wdnj.xxb.subsidy.entity.subsidyInfo;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

/**
 * 描述: ...<br/>
 *
 * @author Knight
 * @version 1.0.0
 * @since 2021-06-11 15:49
 */
@Data
public class ProvinceYearInfo {
    @Alias("label")
    private String province;
    @Alias("years")
    private int[] years;
}
