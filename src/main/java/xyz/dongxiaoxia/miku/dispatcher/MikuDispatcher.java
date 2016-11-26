package xyz.dongxiaoxia.miku.dispatcher;

import com.google.inject.ImplementedBy;
import xyz.dongxiaoxia.miku.context.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 东小侠 on 2016/11/18.
 */
@ImplementedBy(DefaultMikuDispatcher.class)
public interface MikuDispatcher {
    void init();

    void service(HttpServletRequest request, HttpServletResponse response);

    void destroy();

    HttpServletRequest currentRequest();

    HttpServletResponse currentResponse();

    RequestContext currentRequestContext();
}
