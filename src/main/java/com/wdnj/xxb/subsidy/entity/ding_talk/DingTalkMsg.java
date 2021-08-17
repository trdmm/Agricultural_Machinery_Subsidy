package com.wdnj.xxb.subsidy.entity.ding_talk;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Data;

/**
 * 描述: 钉钉消息体<br/>
 *
 * @author Knight
 * @version 1.0.0
 * @since 2021-08-17 14:21
 */
@Data
@Builder
public class DingTalkMsg {
    /** 消息类型--text(其他以后补充) */
    @JSONField(name = "msgtype")
    private String msgType = "text";

    /** 消息 text */
    @JSONField(name = "text")
    private Text text;
}
