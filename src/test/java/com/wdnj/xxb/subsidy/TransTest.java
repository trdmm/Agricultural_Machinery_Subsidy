package com.wdnj.xxb.subsidy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import com.alibaba.excel.EasyExcel;
import com.wdnj.xxb.subsidy.entity.subsidyInfo.SubsidyInfo;
import com.wdnj.xxb.subsidy.excel.SubsidyListener;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 描述: 格式转换<br>
 *
 * @author Knight
 * @version 1.0.0
 * @since 2023-04-17 10:13
 */
@Slf4j
public class TransTest {
  /**
   * 除<江苏省,四川省,内蒙古自治区>外,其他省份的总补贴额那一列后移一列
   */
  @Test
  public void oldFormatToNew(){
    String[] executes = {"江苏省","四川省","内蒙古自治区"};
    Set<String> executesSet = CollUtil.newHashSet(executes);

    String parentPath = "F:\\Projects\\项目文件\\补贴数据爬取\\2023-03-25\\2023-03-25\\补贴查询";

    // 省份目录
    List<File> provinceDirs = FileUtil.loopFiles(FileUtil.file(parentPath), 1, file -> {
      String name = file.getName();
      return !executesSet.contains(name);
    });

    provinceDirs.forEach(provinceDir -> {
      // 每个省的年份目录
      List<File> yearDirs = FileUtil.loopFiles(provinceDir, 1, null);
      yearDirs.forEach(yearDir -> {
        File dstDir = FileUtil.mkdir("F:\\Projects\\项目文件\\补贴数据爬取\\经销商平台写入\\OriginExcelFiles\\2023-04-18\\" + FileUtil.getName(provinceDir) + "\\" + FileUtil.getName(yearDir));
        // 具体的补贴文件
        List<File> files = FileUtil.loopFiles(yearDir, 1, null);

        files.forEach(file -> {
          String name = FileUtil.getName(file);
          List<SubsidyInfo> list = new ArrayList<>();
          EasyExcel.read(file, SubsidyInfo.class, new SubsidyListener(list)).sheet().doRead();
          list.forEach(subsidyInfo -> {
            String subsidyAmounts = subsidyInfo.getSubsidyAmounts();
            String serialNumber = subsidyInfo.getSerialNumber();
            subsidyInfo.setSubsidyAmounts("");
            subsidyInfo.setSerialNumber(subsidyAmounts);
            subsidyInfo.setState(serialNumber);
          });
          EasyExcel.write(FileUtil.getAbsolutePath(dstDir) + "\\" + name, SubsidyInfo.class).sheet().doWrite(list);
        });

      });
    });
    log.info("over");
  }
}
