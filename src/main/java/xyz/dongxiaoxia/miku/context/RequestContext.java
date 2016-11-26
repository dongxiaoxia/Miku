package xyz.dongxiaoxia.miku.context;

import xyz.dongxiaoxia.miku.model.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 东小侠 on 2016/11/18.
 */
public interface RequestContext {

    Model getModel();

    HttpServletRequest getRequest();

    HttpServletResponse getResponse();

}
