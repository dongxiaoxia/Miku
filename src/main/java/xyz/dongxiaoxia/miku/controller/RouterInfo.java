package xyz.dongxiaoxia.miku.controller;

import xyz.dongxiaoxia.miku.annotation.RequestMethod;
import xyz.dongxiaoxia.miku.context.RequestContext;

/**
 * Created by 东小侠 on 2016/11/19.
 * <p>
 * request请求的路由信息
 */
public class RouterInfo {
    /**
     * 请求的 {@link RequestContext}上下文对象
     */
    private final RequestContext requestContext;

    /**
     * 请求的方法
     */
    private final RequestMethod requestMethod;

    /**
     * 请求路径
     */
    private final String path;

    /**
     * 请求路径
     */
    private final String simplePath;

    private RouterInfo(RequestContext requestContext) {
        this.requestContext = requestContext;
        path = requestContext.getRequest().getServletPath();
        simplePath = requestContext.getRequest().getPathInfo();
        String requestMethod = requestContext.getRequest().getMethod().toUpperCase();
        if ("POST".equals(requestMethod)) {
            this.requestMethod = RequestMethod.POST;
        } else if ("GET".equals(requestMethod)) {
            this.requestMethod = RequestMethod.GET;
        } else {
            this.requestMethod = RequestMethod.ALL;
        }
    }

    public RequestContext getRequestContext() {
        return requestContext;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public String getPath() {
        return path;
    }

    public String getSimplePath() {
        return simplePath;
    }

    /**
     * 创建路由信息的静态静态方法
     *
     * @param requestContext 请求上下文
     * @return 对应的路由信息
     */
    public static RouterInfo create(RequestContext requestContext) {
        return new RouterInfo(requestContext);
    }

}
