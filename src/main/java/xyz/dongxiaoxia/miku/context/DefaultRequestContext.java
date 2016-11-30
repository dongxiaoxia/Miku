package xyz.dongxiaoxia.miku.context;

import xyz.dongxiaoxia.miku.model.Model;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 东小侠 on 2016/11/19.
 * <p>
 * 默认的封装管理一个客户端请求的生命周期
 */
public class DefaultRequestContext implements RequestContext {

    /**
     * 请求的 {@link HttpServletRequest}对象
     */
    private final HttpServletRequest request;

    /**
     * 请求的 {@link HttpServletResponse}对象
     */
    private final HttpServletResponse response;

    /**
     * 请求的 {@link Model}对象
     */
    private final Model model;

    @Inject
    public DefaultRequestContext(HttpServletRequest request, HttpServletResponse response, Model model) {
        this.request = request;
        this.response = response;
        this.model = model;
        String encoding = request.getCharacterEncoding();
        if (encoding == null) {
            encoding = "UTF-8";
        }
        this.response.setCharacterEncoding(encoding);
    }

    /**
     * @return 当前请求的 {@link HttpServletRequest}对象
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * @return 当前请求的 {@link HttpServletResponse}对象
     */
    public HttpServletResponse getResponse() {
        return response;
    }

    /**
     * @return 当前请求的 {@link Model}对象。MVC中的Model，以key-value形式存放，可以由Controller传给View。
     */
    public Model getModel() {
        return model;
    }
}
