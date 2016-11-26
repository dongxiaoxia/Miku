package xyz.dongxiaoxia.miku.config;

import com.google.inject.Module;
import xyz.dongxiaoxia.miku.controller.MikuController;

import java.util.Set;

/**
 * Created by 东小侠 on 2016/11/18.
 */
public interface MikuConfig {

    Set<Class<? extends MikuController>> controllerClasses();

    Module module();
}
