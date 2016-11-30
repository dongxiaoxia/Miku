package xyz.dongxiaoxia.miku.dispatcher;

import com.google.inject.ImplementedBy;
import xyz.dongxiaoxia.miku.context.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 东小侠 on 2016/11/18.
 * <p>
 * 用于处理request客户端请求的调度类
 */
@ImplementedBy(DefaultMikuDispatcher.class)
public interface MikuDispatcher {

    /**
     * 调度器初始化
     */
    void init();

    /**
     * 调度分发客户端请求
     *
     * @param request  当前请求的 {@link HttpServletRequest}对象
     * @param response 当前请求的 {@link HttpServletResponse}对象
     */
    void service(HttpServletRequest request, HttpServletResponse response);

    /**
     * 调度器销毁
     */
    void destroy();

    /**
     * @return 当前请求的 {@link HttpServletRequest}对象
     */
    HttpServletRequest currentRequest();

    /**
     * @return 当前请求的 {@link HttpServletResponse}对象
     */
    HttpServletResponse currentResponse();

    /**
     * @return 当前请求的 {@link RequestContext}对象
     */
    RequestContext currentRequestContext();
}
