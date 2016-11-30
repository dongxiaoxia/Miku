package xyz.dongxiaoxia.miku.view;

import xyz.dongxiaoxia.miku.context.RequestContext;

/**
 * Created by 东小侠 on 2016/11/18.
 * <p>
 * 所有Action的返回结果，用于渲染生成显示页面或内容
 */
public interface Render {

    public static final Render NULL = null;

    /**
     * 渲染生成显示页面或内容
     *
     * @param requestContext 需要渲染的请求上下文
     */
    void render(RequestContext requestContext);
}
