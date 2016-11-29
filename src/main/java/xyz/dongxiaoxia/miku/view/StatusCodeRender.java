package xyz.dongxiaoxia.miku.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dongxiaoxia.miku.context.RequestContext;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 01don on 2016/11/29.
 */
@Singleton
public class StatusCodeRender {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatusCodeRender.class);

    public final static Render DEFAULT_404 = requestContext -> {
        try {
            requestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    };

    public static final Render DEFAULT_405 = requestContext -> {
        try {
            requestContext.getResponse().sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    };

    public static void render404(RequestContext requestContext) {
        DEFAULT_404.render(requestContext);
    }

    public static void render405(RequestContext requestContext) {
        DEFAULT_405.render(requestContext);
    }
}
