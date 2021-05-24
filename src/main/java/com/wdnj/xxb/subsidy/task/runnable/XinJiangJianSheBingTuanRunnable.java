package com.wdnj.xxb.subsidy.task.runnable;

import java.io.File;

import com.wdnj.xxb.subsidy.util.SubsidyCommon;
import com.wdnj.xxb.subsidy.util.ConstantUtil;
import com.wdnj.xxb.subsidy.util.SubsidyHttpClient;

/**
 * 描述: 湖北省<br>
 *
 * @version 1.0.0
 * @project: Subsidy
 * @file: HBRunnable
 * @author: Mingming Huang
 * @since: 2021/3/18 14:47
 */
public class XinJiangJianSheBingTuanRunnable implements Runnable {
    private final SubsidyHttpClient httpClient;
    private final File dir;

    public XinJiangJianSheBingTuanRunnable(SubsidyHttpClient subsidyHttpClient, File dir) {
        this.httpClient = subsidyHttpClient;
        this.dir = dir;
    }

    @Override
    public void run() {
        SubsidyCommon.getSubsidy("新疆建设兵团", new int[] {2018, 2019, 2020}, httpClient,
            ConstantUtil.XINJIANGBINGTUAN_PUBLIC_URL, ConstantUtil.XINJIANGBINGTUAN_LIST_URL, dir);
    }
}