package xyz.dongxiaoxia.miku.view;

import xyz.dongxiaoxia.miku.MikuException;
import xyz.dongxiaoxia.miku.context.RequestContext;

import java.io.IOException;

/**
 * Created by 01don on 2016/11/29.
 */
public class RedirectRender implements Render {

    private String url;

    public RedirectRender(String url){
        this.url = url;
    }

    @Override
    public void render(RequestContext requestContext) {
        try {
            requestContext.getResponse().sendRedirect(this.url);
        } catch (IOException e) {
            throw new MikuException(e.getMessage(),e);
        }
    }
}
