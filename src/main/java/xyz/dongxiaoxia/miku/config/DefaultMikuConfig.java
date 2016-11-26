package xyz.dongxiaoxia.miku.config;

import com.google.inject.Module;
import xyz.dongxiaoxia.miku.annotation.Controller;
import xyz.dongxiaoxia.miku.controller.MikuController;
import xyz.dongxiaoxia.miku.MikuModule;
import xyz.dongxiaoxia.miku.utils.ClassUtils;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by 东小侠 on 2016/11/18.
 */
public class DefaultMikuConfig implements MikuConfig {
    @Override
    public Set<Class<? extends MikuController>> controllerClasses() {
        Set<Class<?>> classSet = ClassUtils.getClasses("");
        Pattern controllerPattern = Pattern.compile(".*Controller");
        Set<Class<? extends MikuController>> classes = new HashSet<>();
        for (Class<?> clazz : classSet) {
            if (MikuController.class.isAssignableFrom(clazz)
                    && controllerPattern.matcher(clazz.getName()).matches()
                    && !Modifier.isInterface(clazz.getModifiers())
                    && !Modifier.isAbstract(clazz.getModifiers())
                    && Modifier.isPublic(clazz.getModifiers())
                    && clazz.isAnnotationPresent(Controller.class)){
                classes.add((Class<? extends MikuController>) clazz);
            }
        }
        return classes;
    }

    @Override
    public Module module() {
        return new MikuModule();
    }
}
