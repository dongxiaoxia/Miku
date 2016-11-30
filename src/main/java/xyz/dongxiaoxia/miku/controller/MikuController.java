package xyz.dongxiaoxia.miku.controller;

/**
 * Created by 东小侠 on 2016/11/18.
 * <p>
 * 所有的Controller必须实现的接口
 */
public interface MikuController {

    /**
     * Controller被injector实例化后，将立即调用本方法进行初始化，代替构造函数
     */
    void init();
}
