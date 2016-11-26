package xyz.dongxiaoxia.miku.controller;

import xyz.dongxiaoxia.miku.annotation.RequestMethod;
import xyz.dongxiaoxia.miku.context.RequestContext;

/**
 * Created by 东小侠 on 2016/11/19.
 */
public class RouterInfo {
    private final RequestContext requestContext;
    private final RequestMethod requestMethod;
    private final String path;
    private final String simplePath;

    private RouterInfo(RequestContext requestContext){
        this.requestContext = requestContext;
        path = requestContext.getRequest().getServletPath();
        simplePath = requestContext.getRequest().getPathInfo();
        String  requestMethod = requestContext.getRequest().getMethod().toUpperCase();
        if ("POST".equals(requestMethod)){
            this.requestMethod = RequestMethod.POST;
        }else  if ("GET".equals(requestMethod)){
            this.requestMethod = RequestMethod.GET;
        }else {
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

    public static RouterInfo create(RequestContext requestContext){
        return new RouterInfo(requestContext);
    }

}
