package xyz.dongxiaoxia.miku.config;

import xyz.dongxiaoxia.miku.interceptor.Interceptors;

/**
 * Created by 东小侠 on 2016/11/18.
 * <p>
 * 提供给客户端的配置类，默认什么多钟不处理，客户端通过继承该类修改配置内容。
 */
public class MikuConfig {

    /**
     * @param me 配置系统常量
     */
    public void configConstants(Constants me) {

    }

    /**
     * 配置Guice IOC
     *
     * @param me
     */
    public void configModules(Modules me) {

    }

    /**
     * 配置拦截器
     *
     * @param me
     */
    public void configInterceptors(Interceptors me) {

    }
}
