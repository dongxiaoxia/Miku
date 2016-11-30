package xyz.dongxiaoxia.miku.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dongxiaoxia.miku.config.Constants;
import xyz.dongxiaoxia.miku.Miku;
import xyz.dongxiaoxia.miku.config.MikuConfig;
import xyz.dongxiaoxia.miku.MikuException;
import xyz.dongxiaoxia.miku.config.Modules;
import xyz.dongxiaoxia.miku.interceptor.Interceptors;

import javax.servlet.ServletContext;

/**
 * Created by 东小侠 on 2016/11/18.
 * <p>
 * {@link MikuDispatcher}调度器工厂类
 */
public class MikuDispatcherFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(MikuDispatcherFactory.class);

    public static MikuDispatcher create(ServletContext servletContext) {
        String className = servletContext.getInitParameter("configClass");
        MikuConfig mikuConfig;
        if (className == null || className.trim().equals("")) {
            mikuConfig = new MikuConfig();
        } else {
            try {
                Class clazz = Class.forName(className);
                if (!MikuConfig.class.isAssignableFrom(clazz)) {
                    mikuConfig = new MikuConfig();
                } else {
                    mikuConfig = (MikuConfig) clazz.newInstance();
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                throw new MikuException(e.getMessage(), e);
            }
        }

        Constants constants = new Constants();
        Modules modules = new Modules();
        Interceptors interceptors = new Interceptors();
        mikuConfig.configConstants(constants);
        mikuConfig.configModules(modules);
        mikuConfig.configInterceptors(interceptors);
        return Miku.me.init(constants, modules, interceptors);
    }
}
