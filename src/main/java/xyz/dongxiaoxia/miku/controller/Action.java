package xyz.dongxiaoxia.miku.controller;

import xyz.dongxiaoxia.miku.view.Render;

/**
 * Created by 东小侠 on 2016/11/19.
 */
public interface Action {
    Render matchAndInvoke(RouterInfo routerInfo);
}
