package xyz.dongxiaoxia.miku.config;

import com.google.inject.Module;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 东小侠 on 2016/11/27.
 * <p>
 * 框架的Guice IOC {@link Module} 配置类
 */
public class Modules {

    private List<Module> modules;

    public Modules() {
        this.modules = new ArrayList<>();
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public void add(Module module) {
        this.modules.add(module);
    }
}
