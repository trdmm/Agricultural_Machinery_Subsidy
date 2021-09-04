package com.wdnj.xxb.subsidy.entity.subsidyInfo.new_app;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class RowsItem{

	@JSONField(name="purchaseDate")
	private String purchaseDate;

	@JSONField(name="maxName")
	private Object maxName;

	@JSONField(name="centralSubsidy")
	private Object centralSubsidy;

	@JSONField(name="everySalePrice")
	private double everySalePrice;

	@JSONField(name="pcode")
	private Object pcode;

	@JSONField(name="applicationNo")
	private String applicationNo;

	@JSONField(name="sumSalePrice")
	private Object sumSalePrice;

	@JSONField(name="county")
	private String county;

	@JSONField(name="townSubsidy")
	private Object townSubsidy;

	@JSONField(name="title")
	private Object title;

	@JSONField(name="factoryNumber")
	private String factoryNumber;

	@JSONField(name="years")
	private int years;

	@JSONField(name="productName")
	private String productName;

	@JSONField(name="scrapSubsidy")
	private Object scrapSubsidy;

	@JSONField(name="benefitNumberQ")
	private Object benefitNumberQ;

	@JSONField(name="itemName")
	private String itemName;

	@JSONField(name="equipmentNoSum")
	private Object equipmentNoSum;

	@JSONField(name="everySubsidy")
	private double everySubsidy;

	@JSONField(name="minName")
	private Object minName;

	@JSONField(name="id")
	private int id;

	@JSONField(name="village")
	private Object village;

	@JSONField(name="countySubsidy")
	private Object countySubsidy;

	@JSONField(name="manufacturingNumber")
	private Object manufacturingNumber;

	@JSONField(name="provSubsidy")
	private Object provSubsidy;

	@JSONField(name="machineCode")
	private String machineCode;

	@JSONField(name="town")
	private String town;

	@JSONField(name="buyName")
	private String buyName;

	@JSONField(name="citySubsidy")
	private Object citySubsidy;

	@JSONField(name="factoryName")
	private String factoryName;

	@JSONField(name="agentName")
	private String agentName;

	@JSONField(name="equipmentNo")
	private String equipmentNo;

	@JSONField(name="sumSubsidy")
	private double sumSubsidy;

	@JSONField(name="xcode")
	private Object xcode;

	@JSONField(name="benefitNumber")
	private Object benefitNumber;

	@JSONField(name="implementationFunds")
	private Object implementationFunds;

	@JSONField(name="proEverySubsidy")
	private double proEverySubsidy;

	@JSONField(name="scrapProvSubsidy")
	private Object scrapProvSubsidy;

	@JSONField(name="dcode")
	private Object dcode;

	@JSONField(name="status")
	private String status;
}