package com.wdnj.xxb.subsidy.entity.subsidyInfo.new_app;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class RequestBodyNewer {

	@JSONField(name="itemName")
	private String itemName;

	@JSONField(name="areaCode")
	private String areaCode;

	@JSONField(name="year")
	private int year;

  @JSONField(name="code")
  private String code;

	@JSONField(name="purchaseDateStart")
	private String purchaseDateStart;

	@JSONField(name="enterpriseId")
	private String enterpriseId;

	@JSONField(name="purchaseDateEnd")
	private String purchaseDateEnd;

	@JSONField(name="buyerName")
	private String buyerName;

	@JSONField(name="distributor")
	private String distributor;

	@JSONField(name="status")
	private String status;

    public RequestBodyNewer(int year) {
        this.year = year;
    }
}