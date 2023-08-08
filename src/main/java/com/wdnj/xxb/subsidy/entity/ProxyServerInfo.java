package com.wdnj.xxb.subsidy.entity;

import lombok.Data;

/**
 * 描述: 代理服务器信息<br>
 *
 * @author Knight
 * @version 1.0.0
 * @since 2022-08-30 16:11
 */
@Data
public class ProxyServerInfo {
    /** 代理服务器地址 */
    private String proxyServerHost;
    /** 代理服务器端口 */
    private int proxyServerPort;
}
