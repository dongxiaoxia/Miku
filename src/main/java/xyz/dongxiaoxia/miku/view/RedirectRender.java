package xyz.dongxiaoxia.miku.view;

import xyz.dongxiaoxia.miku.MikuException;
import xyz.dongxiaoxia.miku.context.RequestContext;

import java.io.IOException;

/**
 * Created by 东小侠 on 2016/11/29.
 * 跳转渲染类
 */
public class RedirectRender implements Render {

    /**
     * 要跳转的url
     */
    private String url;

    public RedirectRender(String url) {
        this.url = url;
    }

    /**
     * 跳转到对应的url
     *
     * @param requestContext 需要渲染的请求上下文
     */
    @Override
    public void render(RequestContext requestContext) {
        try {
            requestContext.getResponse().sendRedirect(this.url);
        } catch (IOException e) {
            throw new MikuException(e.getMessage(), e);
        }
    }
}
