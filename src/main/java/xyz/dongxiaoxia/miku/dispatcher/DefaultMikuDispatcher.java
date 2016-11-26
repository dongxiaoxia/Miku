package xyz.dongxiaoxia.miku.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dongxiaoxia.miku.Miku;
import xyz.dongxiaoxia.miku.MikuException;
import xyz.dongxiaoxia.miku.annotation.Controller;
import xyz.dongxiaoxia.miku.context.DefaultRequestContext;
import xyz.dongxiaoxia.miku.context.RequestContext;
import xyz.dongxiaoxia.miku.controller.Action;
import xyz.dongxiaoxia.miku.controller.ControllerInfo;
import xyz.dongxiaoxia.miku.controller.MikuController;
import xyz.dongxiaoxia.miku.controller.RouterInfo;
import xyz.dongxiaoxia.miku.model.DefaultModel;
import xyz.dongxiaoxia.miku.utils.ClassUtils;
import xyz.dongxiaoxia.miku.view.Render;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by 东小侠 on 2016/11/18.
 */
@Singleton
public class DefaultMikuDispatcher implements MikuDispatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMikuDispatcher.class);
    private final ThreadLocal<RequestContext> localContext = new ThreadLocal<>();
    private final List<Action> actions;

    @Inject
    public DefaultMikuDispatcher() {
        List<Action> actions = new ArrayList<>();
        Set<Class<?>> classSet = ClassUtils.getClasses(Miku.me.constants().getControllerPath());
        Pattern controllerPattern = Pattern.compile(".*Controller");
        Set<Class<? extends MikuController>> controllerClasses = new HashSet<>();
        for (Class<?> clazz : classSet) {
            if (MikuController.class.isAssignableFrom(clazz)
                    && controllerPattern.matcher(clazz.getName()).matches()
                    && !Modifier.isInterface(clazz.getModifiers())
                    && !Modifier.isAbstract(clazz.getModifiers())
                    && Modifier.isPublic(clazz.getModifiers())
                    && clazz.isAnnotationPresent(Controller.class)) {
                controllerClasses.add((Class<? extends MikuController>) clazz);
            }
        }
        if (!controllerClasses.isEmpty()) {
            for (Class<? extends MikuController> controllerClass : controllerClasses) {
                MikuController controller = Miku.me.getInstance(controllerClass);
                controller.init();
                ControllerInfo controllerInfo = new ControllerInfo(controller);
                actions.addAll(controllerInfo.analyze());
            }
        }
        //// TODO: 2016/11/21
//        StaticAction staticAction = new StaticAction();
//        actions.add(staticAction);
        this.actions = actions;
    }

    @Override
    public void init() {

    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) {
        RequestContext context = new DefaultRequestContext(request, response, new DefaultModel());
        localContext.set(context);
        try {
            Render render = null;
            RouterInfo routerInfo = RouterInfo.create(context);
            for (Action action : actions) {
                render = action.matchAndInvoke(routerInfo);
                if (render != null) {
                    break;
                }
            }
            if (render == null) {
                context.getResponse().setStatus(404);
            } else {
//                render.render(requestContext);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            context.getResponse().setStatus(405);
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public HttpServletRequest currentRequest() {
        return currentRequestContext().getRequest();
    }

    @Override
    public HttpServletResponse currentResponse() {
        return currentRequestContext().getResponse();
    }

    @Override
    public RequestContext currentRequestContext() {
        RequestContext context = localContext.get();
        if (context == null) {
            throw new MikuException("Cannot access scoped object. Either we"
                    + " are not currently inside an HTTP Servlet currentRequest, or you may"
                    + " have forgotten to apply " + DefaultMikuDispatcher.class.getName()
                    + " as a servlet filter for this currentRequest.");
        }
        return context;
    }

}
