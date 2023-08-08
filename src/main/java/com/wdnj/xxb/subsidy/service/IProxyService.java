package com.wdnj.xxb.subsidy.service;

import com.wdnj.xxb.subsidy.entity.ProxyServerInfo;

/**
 * 描述: 代理服务器 Service<br>
 *
 * @author Knight
 * @version 1.0.0
 * @since 2022-08-30 16:07
 */
public interface IProxyService {
    /**
     * 获取一个代理服务器
     * @return 一个代理服务器信息
     */
    ProxyServerInfo getProxyServer();
}
