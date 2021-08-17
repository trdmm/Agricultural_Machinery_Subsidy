package com.wdnj.xxb.subsidy.entity.ding_talk;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 描述: 钉钉机器人消息 text 类型的 text<br/>
 *
 * @author Knight
 * @version 1.0.0
 * @since 2021-08-17 14:23
 */
@Data
public class Text {
    /** 消息内容 */
    @JSONField(name = "content")
    private String content;

    public Text(String content) {
        this.content = content;
    }
}
