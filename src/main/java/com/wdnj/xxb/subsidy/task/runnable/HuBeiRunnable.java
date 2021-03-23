package com.wdnj.xxb.subsidy.task.runnable;

import com.wdnj.xxb.subsidy.task.SubsidyCommon;
import com.wdnj.xxb.subsidy.util.ConstantUtil;
import com.wdnj.xxb.subsidy.util.SubsidyHttpClient;

import java.io.File;

/**
 * 描述: 湖北省<br>
 *
 * @version 1.0.0
 * @project: Subsidy
 * @file: HBRunnable
 * @author: Mingming Huang
 * @since: 2021/3/18 14:47
 */
public class HuBeiRunnable implements Runnable {
    private final SubsidyHttpClient httpClient;
    private final File dir;

    public HuBeiRunnable(SubsidyHttpClient subsidyHttpClient, File dir) {
        this.httpClient = subsidyHttpClient;
        this.dir = dir;
    }

    @Override
    public void run() {
        SubsidyCommon.getSubsidy("湖北",new int[]{2018,2019,2020},httpClient,
            ConstantUtil.HUBEI_PUBLIC_URL,ConstantUtil.HUBEI_LIST_URL, dir);
    }
}
