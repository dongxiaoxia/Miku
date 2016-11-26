package xyz.dongxiaoxia.miku.context;

import xyz.dongxiaoxia.miku.model.Model;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 东小侠 on 2016/11/19.
 */
public class DefaultRequestContext implements RequestContext {
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final Model model;

    @Inject
    public DefaultRequestContext(HttpServletRequest request,HttpServletResponse response,Model model){
        this.request = request;
        this.response = response;
        this.model = model;
        String encoding = request.getCharacterEncoding();
        if (encoding == null) {
            encoding = "UTF-8";
        }
        this.response.setCharacterEncoding(encoding);
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public Model getModel() {
        return model;
    }
}
