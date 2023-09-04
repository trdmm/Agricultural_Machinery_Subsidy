package com.wdnj.xxb.subsidy.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.wdnj.xxb.subsidy.entity.ProxyServerInfo;
import com.wdnj.xxb.subsidy.http.SubsidyHttpClient;
import com.wdnj.xxb.subsidy.service.IProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述: 代理服务器Service实现类<br>
 *
 * @author Knight
 * @version 1.0.0
 * @since 2022-08-30 16:09
 */
@Service
public class ProxyServiceImpl implements IProxyService {
  @Autowired
  private SubsidyHttpClient httpClient;

  @Override
  public ProxyServerInfo getProxyServer() {
    // TODO(获取代理服务器)
    String proxyInfo = httpClient.getProxyInfo();
    if (StrUtil.isNotBlank(proxyInfo)) {
      String[] split = StrUtil.splitToArray(proxyInfo, ':');
      ProxyServerInfo proxyServerInfo = new ProxyServerInfo();
      proxyServerInfo.setProxyServerHost(split[0]);
      proxyServerInfo.setProxyServerPort(NumberUtil.parseInt(split[1]));

      return proxyServerInfo;
    }
    return null;
  }
}
