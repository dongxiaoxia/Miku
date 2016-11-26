package xyz.dongxiaoxia.miku.dispatcher;

import xyz.dongxiaoxia.miku.Miku;
import xyz.dongxiaoxia.miku.config.DefaultMikuConfig;
import xyz.dongxiaoxia.miku.config.MikuConfig;

/**
 * Created by 东小侠 on 2016/11/18.
 */
public class MikuDispatcherFactory {

    public static MikuDispatcher create() {
        MikuConfig mikuConfig = new DefaultMikuConfig();
        return Miku.me.init(mikuConfig);
    }

}
