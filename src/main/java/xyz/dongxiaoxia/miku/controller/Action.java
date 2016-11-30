package xyz.dongxiaoxia.miku.controller;

import xyz.dongxiaoxia.miku.view.Render;

/**
 * Created by 东小侠 on 2016/11/19.
 * <p>
 * 处理request客户端请求的封装对象
 */
public interface Action {

    /**
     * 匹配路由信息并且处理请求
     *
     * @param routerInfo 当前请求的路由信息
     * @return 匹配或指向处理请求结果
     */
    Render matchAndInvoke(RouterInfo routerInfo);
}
