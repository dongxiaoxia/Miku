package xyz.dongxiaoxia.miku.view;

import xyz.dongxiaoxia.miku.Miku;
import xyz.dongxiaoxia.miku.MikuException;
import xyz.dongxiaoxia.miku.context.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 东小侠 on 2016/11/20.
 */
public class JspRender implements Render {

    private String viewPath;

    public JspRender(String viewPath) {
        this.viewPath = viewPath;
    }

    @Override
    public void render(RequestContext requestContext) {
        HttpServletRequest request = requestContext.getRequest();
        HttpServletResponse response = requestContext.getResponse();

        try {
            request.getRequestDispatcher(Miku.me.constants().getViewPrefix()+ viewPath + Miku.me.constants().getViewSuffix()).forward(request, response);
        } catch (Exception e){
            throw new MikuException(e.getMessage(),e);
        }
    }
}
