package xyz.dongxiaoxia.miku.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dongxiaoxia.miku.Miku;
import xyz.dongxiaoxia.miku.context.RequestContext;
import xyz.dongxiaoxia.miku.model.Model;
import xyz.dongxiaoxia.miku.view.JspRender;
import xyz.dongxiaoxia.miku.view.PrintWriterRender;
import xyz.dongxiaoxia.miku.view.RedirectRender;
import xyz.dongxiaoxia.miku.view.Render;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 东小侠 on 2016/11/29.
 * <p>
 * 提供一个基础的抽象的Controller实现，并提供一些Controller实现所常用的方法。
 */
public abstract class AbstractController implements MikuController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void init() {
        logger.debug(getClass().getName() + " is initialize.");
    }

    /**
     * 返回一个view,系统默认采用jsp实现，默认的前置/WEB-INF/,默认后缀为.jsp
     * 可以通过配置更改
     *
     * @param viewName 视图路径名称
     * @return view视图
     * @see xyz.dongxiaoxia.miku.config.Constants
     */
    protected Render view(String viewName) {
        return new JspRender(viewName);
    }

    /**
     * 跳转到一个新页面
     * @param url
     * @return
     */
    protected Render redirect(String url){
        return new RedirectRender(url);
    }

    /**
     * 获取当前请求的model
     * @return
     */
    protected Model getModel(){
        return getRequestContext().getModel();
    }

    /**
     * 获取当前请求的上下文
     * @return
     */
    protected RequestContext getRequestContext(){
        return Miku.me.dispatcher().currentRequestContext();
    }

    /**
     * 获取当前请求的request
     * @return
     */
    protected HttpServletRequest getRequest(){
        return getRequestContext().getRequest();
    }

    /**
     * 获取当前请求的response
     * @return
     */
    protected HttpServletResponse getResponse(){
        return getRequestContext().getResponse();
    }

    /**
     * 以原始的方式提供写response的方法
     * @return
     */
    protected PrintWriterRender getWriter(){
        return PrintWriterRender.create(getResponse());
    }

}
