package com.wdnj.xxb.subsidy.task.fhrunnable;

import java.io.File;

import com.wdnj.xxb.subsidy.util.SubsidyCommon;
import com.wdnj.xxb.subsidy.util.ConstantUtil;
import com.wdnj.xxb.subsidy.http.SubsidyHttpClient;

/**
 * 描述: 浙江企业供货 Runnable<br/>
 *
 * @author Knight
 * @version 1.0.0
 * @since 2021-05-20 10:36
 */
public class GH_ZheJiangRunnable implements Runnable {
    private final SubsidyHttpClient subsidyHttpClient;
    private final File dir;

    public GH_ZheJiangRunnable(SubsidyHttpClient subsidyHttpClient, File dir) {
        this.subsidyHttpClient = subsidyHttpClient;
        this.dir = dir;
    }

    @Override
    public void run() {
        SubsidyCommon.getFhInfo("浙江", subsidyHttpClient, dir, ConstantUtil.QIYEGONGHUO_ZHEJIANG);
    }
}
