package com.wdnj.xxb.subsidy.http;

import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.interceptor.Interceptor;
import com.google.common.util.concurrent.RateLimiter;

/**
 * 描述: 钉钉限流拦截器<br>
 *
 * @author Knight
 * @version 1.0.0
 * @since 2022-07-22 12:47
 */
public class RateLimiterInterceptor<T> implements Interceptor<T> {
    private final RateLimiter RATE_LIMITER = RateLimiter.create(0.3);
    @Override
    public boolean beforeExecute(ForestRequest request) {
        double acquire = RATE_LIMITER.acquire();
        return true;
    }
}
