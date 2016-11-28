package xyz.dongxiaoxia.miku.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 东小侠 on 2016/11/27.
 * <p>
 * 拦截器接口
 */
public interface Interceptor {

    /**
     * Action之前执行，返回true，往下执行，返回false，结束接下来的请求执行
     *
     * @param request
     * @param response
     * @param o
     * @return
     * @throws Exception
     */
    boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception;

    /**
     * Action执行完成后执行
     *
     * @param request
     * @param response
     * @param o
     * @throws Exception
     */
    void postHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception;
}
