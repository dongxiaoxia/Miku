package xyz.dongxiaoxia.miku.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dongxiaoxia.miku.Miku;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by 东小侠 on 2016/11/23.
 */
@WebFilter(filterName = "MikuFilter" ,urlPatterns = "/*" )
public class MikuFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(MikuFilter.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("MikuFilter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (Miku.me.constants().isDevMode()){
            logger.debug("----------------------------------------------------------------------");
            logger.debug("执行请求：" + ((HttpServletRequest) servletRequest).getServletPath());
            logger.debug("----------------------------------------------------------------------");
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
        System.out.println("MikuFilter destroy.");
    }
}
