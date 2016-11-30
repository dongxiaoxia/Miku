package xyz.dongxiaoxia.miku.context;

import xyz.dongxiaoxia.miku.model.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 东小侠 on 2016/11/18.
 * <p>
 * 封装管理一个客户端请求的生命周期
 */
public interface RequestContext {

    /**
     * @return 当前请求的 {@link Model}对象。MVC中的Model，以key-value形式存放，可以由Controller传给View。
     */
    Model getModel();

    /**
     * @return 当前请求的 {@link HttpServletRequest}对象
     */
    HttpServletRequest getRequest();

    /**
     * @return 当前请求的 {@link HttpServletResponse}对象
     */
    HttpServletResponse getResponse();

}
