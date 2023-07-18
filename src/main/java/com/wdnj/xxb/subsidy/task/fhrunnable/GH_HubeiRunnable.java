package com.wdnj.xxb.subsidy.task.fhrunnable;

import java.io.File;

import com.wdnj.xxb.subsidy.util.SubsidyCommon;
import com.wdnj.xxb.subsidy.util.ConstantUtil;
import com.wdnj.xxb.subsidy.http.SubsidyHttpClient;

/**
 * 描述: 湖北企业供货 Runnable<br/>
 *
 * @author Knight
 * @version 1.0.0
 * @since 2021-05-19 15:42
 */
public class GH_HubeiRunnable implements Runnable {
    private final SubsidyHttpClient subsidyHttpClient;
    private final File dir;

    public GH_HubeiRunnable(SubsidyHttpClient subsidyHttpClient, File dir) {
        this.subsidyHttpClient = subsidyHttpClient;
        this.dir = dir;
    }

    @Override
    public void run() {
        SubsidyCommon.getFhInfo("湖北", subsidyHttpClient, dir, ConstantUtil.QIYEGONGHUO_HUBEI);
    }
}
