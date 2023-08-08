package com.wdnj.xxb.subsidy.http;

import com.dtflys.forest.http.ForestProxy;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.interceptor.Interceptor;
import com.google.common.util.concurrent.RateLimiter;
import com.wdnj.xxb.subsidy.entity.ProxyServerInfo;
import com.wdnj.xxb.subsidy.service.IProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 描述: 钉钉限流拦截器<br>
 *
 * @author Knight
 * @version 1.0.0
 * @since 2022-07-22 12:47
 */
@Component
public class ProxyInterceptor<T> implements Interceptor<T> {
    private final IProxyService proxyService;

    public ProxyInterceptor(IProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @Override
    public boolean beforeExecute(ForestRequest request) {
        // 获取代理服务器信息
        ProxyServerInfo proxyServerInfo = proxyService.getProxyServer();
        if (proxyServerInfo == null) {
            return false;
        }
        ForestProxy forestProxy = new ForestProxy(proxyServerInfo.getProxyServerHost(), proxyServerInfo.getProxyServerPort());
        request.setProxy(forestProxy);
        return true;
    }
}
