package xyz.dongxiaoxia.miku.interceptor;

import java.util.List;

/**
 * Created by 东小侠 on 2016/11/27.
 * <p>
 * 拦截器映射信息类
 */
public class InterceptorInfo {

    private List<String> mapping;  //拦截器映射的url
    private List<String> excludeMapping; //拦截器不映射的url
    private Class<? extends Interceptor> interceptor;//具体的拦截器

    public List<String> getMapping() {
        return mapping;
    }

    public void setMapping(List<String> mapping) {
        this.mapping = mapping;
    }

    public List<String> getExcludeMapping() {
        return excludeMapping;
    }

    public void setExcludeMapping(List<String> excludeMapping) {
        this.excludeMapping = excludeMapping;
    }

    public Class<? extends Interceptor> getInterceptor() {
        return interceptor;
    }

    public void setInterceptor(Class<? extends Interceptor> interceptor) {
        this.interceptor = interceptor;
    }
}
