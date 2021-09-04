package com.wdnj.xxb.subsidy.entity.subsidyInfo.new_app;

import java.util.List;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class SubsidyInfoResponse {

	@JSONField(name="msg")
	private Object msg;

	@JSONField(name="total")
	private int total;

	@JSONField(name="code")
	private int code;

	@JSONField(name="rows")
	private List<RowsItem> rows;
}