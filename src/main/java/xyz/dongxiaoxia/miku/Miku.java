package xyz.dongxiaoxia.miku;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dongxiaoxia.miku.config.Constants;
import xyz.dongxiaoxia.miku.config.Modules;
import xyz.dongxiaoxia.miku.dispatcher.MikuDispatcher;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 东小侠 on 2016/11/18.
 * <p>
 * Miku 是整个框架的根对象，负责环境的管理和各个对象的创建。
 */
public class Miku {
    public static final Miku me = new Miku();
    private static final Logger logger = LoggerFactory.getLogger(Miku.class);
    private Injector injector;
    private volatile MikuDispatcher mikuDispatcher;
    private Constants constants;
    private Modules modules;
    private static boolean isInit = false;

    private Miku() {
    }

    /**
     * 框架初始化
     * <p>
     * 根据MikuConfig配置类初始化整个框架，包括处理配置文件，扫描Controller,创建Guice注入对象，初始化MikuDispatcher对象。
     *
     * @param
     * @return
     */
    public MikuDispatcher init(Constants constants, Modules modules) {
        if (isInit) return me.mikuDispatcher;//确保初始化一次
        logger.info("initializing Miku...");
        this.constants = constants;
        this.modules = modules;
        logger.info("preparing an injector");
        this.injector = Guice.createInjector(modules.getModules());
        logger.info("injector completed");
        logger.info("preparing an MikuDispatcher");
        this.mikuDispatcher = getInstance(MikuDispatcher.class);
        logger.info("the MikuDispatcher completed");
        logger.info("Miku initialized");
        printBanner();
        return mikuDispatcher;
    }

    private void printBanner() {
        InputStream in = Miku.class.getClassLoader().getResourceAsStream("banner.txt");
        try {
            byte[] bytes = new byte[in.available()];
            in.read(bytes);
            in.close();
            logger.info("\n"+new String(bytes,"utf-8"));
        } catch (IOException e) {
        }
    }

    public Injector injector() {
        return injector;
    }

    public <T> T getInstance(Class<T> type) {
        return injector().getInstance(type);
    }

    public MikuDispatcher dispatcher() {
        return mikuDispatcher;
    }

    public Constants constants() {
        return constants;
    }
}
