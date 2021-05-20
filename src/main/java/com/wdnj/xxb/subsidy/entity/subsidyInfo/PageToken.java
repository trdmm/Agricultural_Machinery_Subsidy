package com.wdnj.xxb.subsidy.entity.subsidyInfo;

import lombok.Builder;
import lombok.Data;

/**
 * 描述: 公示页的页数和token<br>
 *
 * @version 1.0.0
 * @project: Subsidy
 * @file: PageToken
 * @author: Mingming Huang
 * @since: 2021/3/3 19:38
 */
@Builder
@Data
public class PageToken {
  private int pages;
  private String token;
}
