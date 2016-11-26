package xyz.dongxiaoxia.miku.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dongxiaoxia.miku.dispatcher.MikuDispatcher;
import xyz.dongxiaoxia.miku.dispatcher.MikuDispatcherFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 东小侠 on 2016/11/18.
 *
 * 框架入口，负责处理接收到的所有请求，派发到各个Controller处理，在项目启动时触发框架初始化。
 */
@WebServlet(urlPatterns = {"/"}, name = "MikuDispatcherServlet",loadOnStartup = 1)
public class MikuDispatcherServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(MikuDispatcherServlet.class);

    private MikuDispatcher dispatcher;

    @Override
    public void init() throws ServletException {
        super.init();
        logger.info(this.getClass().getName() + ".init()");
        try {
            dispatcher = MikuDispatcherFactory.create();
            dispatcher.init();
        } catch (Exception e) {
            logger.error("failed to Miku initialize,system exit!!!", e);
            System.exit(1);
        }
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        dispatcher.service(req, res);
    }

    @Override
    public void destroy() {
        dispatcher.destroy();
        super.destroy();
    }
}
