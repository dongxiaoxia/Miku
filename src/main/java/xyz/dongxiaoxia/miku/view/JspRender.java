package xyz.dongxiaoxia.miku.view;

import xyz.dongxiaoxia.miku.Miku;
import xyz.dongxiaoxia.miku.MikuException;
import xyz.dongxiaoxia.miku.context.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 东小侠 on 2016/11/20.
 * <p>
 * JSP页面渲染类
 */
public class JspRender implements Render {

    /**
     * JSP页面对应的路径（不包括前缀与后缀）
     */
    private String viewPath;

    public JspRender(String viewPath) {
        this.viewPath = viewPath;
    }

    /**
     * 渲染JSP页面
     *
     * @param requestContext 需要渲染的请求上下文
     */
    @Override
    public void render(RequestContext requestContext) {
        HttpServletRequest request = requestContext.getRequest();
        HttpServletResponse response = requestContext.getResponse();
        try {
            request.getRequestDispatcher(Miku.me.constants().getViewPrefix() + viewPath + Miku.me.constants().getViewSuffix()).forward(request, response);
        } catch (Exception e) {
            throw new MikuException(e.getMessage(), e);
        }
    }
}
