package com.wdnj.xxb.subsidy.task.runnable;

import java.io.File;

import com.wdnj.xxb.subsidy.util.SubsidyCommon;
import com.wdnj.xxb.subsidy.util.ConstantUtil;
import com.wdnj.xxb.subsidy.util.SubsidyHttpClient;

/**
 * 描述:...<br>
 *
 * @version 1.0.0
 * @project: Subsidy
 * @file: BJRunnable
 * @author: Mingming Huang
 * @since: 2021/3/18 17:55
 */
public class JiLinRunnable implements Runnable {
    private final SubsidyHttpClient httpClient;
    private final File dir;

    public JiLinRunnable(SubsidyHttpClient subsidyHttpClient, File dir) {
        this.httpClient = subsidyHttpClient;
        this.dir = dir;
    }

    @Override
    public void run() {
        SubsidyCommon.getSubsidy("吉林",new int[]{2019,2020},httpClient,
            ConstantUtil.JILIN_PUBLIC_URL,ConstantUtil.JILIN_LIST_URL, dir);
    }
}
