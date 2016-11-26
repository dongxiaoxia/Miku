package xyz.dongxiaoxia.miku.controller;

import xyz.dongxiaoxia.miku.view.JspRender;
import xyz.dongxiaoxia.miku.view.Render;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by 东小侠 on 2016/11/21.
 */
public class StaticAction implements Action {

    private Set<String> staticFiles = new HashSet<>();

    @Override
    public Render matchAndInvoke(RouterInfo routerInfo) {
        Render render = new JspRender(routerInfo.getPath());
        render.render(routerInfo.getRequestContext());
        return render;
    }

    public StaticAction(){
        staticFiles.add("/index.html");
    }
}
