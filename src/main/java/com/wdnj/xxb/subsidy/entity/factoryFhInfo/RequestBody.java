package com.wdnj.xxb.subsidy.entity.factoryFhInfo;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

/**
 * 描述:... <br/>
 *
 * @author Knight
 * @version 1.0.0
 * @since 2021-05-19 16:18
 */
@Data
public class RequestBody {
    @Alias("IsFaH")
    private int isFaH;
    @Alias("PageSize")
    private int pageSize;
    //@Alias("pageIndex")
    //private int pageIndex;
}
