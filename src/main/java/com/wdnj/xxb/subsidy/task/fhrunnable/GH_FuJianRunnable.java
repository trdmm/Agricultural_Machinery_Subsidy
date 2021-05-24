package com.wdnj.xxb.subsidy.task.fhrunnable;

import java.io.File;

import com.wdnj.xxb.subsidy.util.SubsidyCommon;
import com.wdnj.xxb.subsidy.util.ConstantUtil;
import com.wdnj.xxb.subsidy.util.SubsidyHttpClient;

/**
 * 描述: 福建企业供货 Runnable<br/>
 *
 * @author Knight
 * @version 1.0.0
 * @since 2021-05-20 10:37
 */
public class GH_FuJianRunnable implements Runnable {
    private final SubsidyHttpClient subsidyHttpClient;
    private final File dir;

    public GH_FuJianRunnable(SubsidyHttpClient subsidyHttpClient, File dir) {
        this.subsidyHttpClient = subsidyHttpClient;
        this.dir = dir;
    }

    @Override
    public void run() {
        SubsidyCommon.getFhInfo("福建", subsidyHttpClient, dir, ConstantUtil.QIYEGONGHUO_FUJIAN);
    }
}
