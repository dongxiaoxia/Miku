package xyz.dongxiaoxia.miku.interceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 东小侠 on 2016/11/27.
 * <p>
 * 拦截器配置类
 */
public final class Interceptors {

    private List<InterceptorInfo> interceptors;

    public Interceptors() {
        interceptors = new ArrayList<>();
    }

    public List<InterceptorInfo> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<InterceptorInfo> interceptors) {
        this.interceptors = interceptors;
    }

    public void add(Class<? extends Interceptor> interceptor, String... mapping) {
        InterceptorInfo interceptorInfo = new InterceptorInfo();
        interceptorInfo.setInterceptor(interceptor);
        interceptorInfo.setMapping(Arrays.asList(mapping));
        this.interceptors.add(interceptorInfo);
    }

    public void add(Class<? extends Interceptor> interceptor, List<String> mapping) {
        InterceptorInfo interceptorInfo = new InterceptorInfo();
        interceptorInfo.setInterceptor(interceptor);
        interceptorInfo.setMapping(mapping);
        this.interceptors.add(interceptorInfo);
    }

    public void add(Class<? extends Interceptor> interceptor, List<String> mapping, List<String> excludeMapping) {
        InterceptorInfo interceptorInfo = new InterceptorInfo();
        interceptorInfo.setInterceptor(interceptor);
        interceptorInfo.setMapping(mapping);
        interceptorInfo.setExcludeMapping(excludeMapping);
        this.interceptors.add(interceptorInfo);
    }
}
