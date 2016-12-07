package xyz.dongxiaoxia.miku.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dongxiaoxia.miku.Miku;
import xyz.dongxiaoxia.miku.MikuException;
import xyz.dongxiaoxia.miku.annotation.Controller;
import xyz.dongxiaoxia.miku.context.DefaultRequestContext;
import xyz.dongxiaoxia.miku.context.MikuRequest;
import xyz.dongxiaoxia.miku.context.RequestContext;
import xyz.dongxiaoxia.miku.controller.Action;
import xyz.dongxiaoxia.miku.controller.ControllerInfo;
import xyz.dongxiaoxia.miku.controller.MikuController;
import xyz.dongxiaoxia.miku.controller.RouterInfo;
import xyz.dongxiaoxia.miku.interceptor.Interceptor;
import xyz.dongxiaoxia.miku.interceptor.InterceptorInfo;
import xyz.dongxiaoxia.miku.model.DefaultModel;
import xyz.dongxiaoxia.miku.utils.ClassUtils;
import xyz.dongxiaoxia.miku.view.Render;
import xyz.dongxiaoxia.miku.view.StatusCodeRender;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static xyz.dongxiaoxia.miku.Miku.me;

/**
 * Created by 东小侠 on 2016/11/18.
 * <p>
 * 框架默认的用于处理request客户端请求的调度类
 */
@Singleton
public class DefaultMikuDispatcher implements MikuDispatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMikuDispatcher.class);

    /**
     * 上传文件配置（Guice控制单例）
     */
    MultipartConfigElement config;

    /**
     * 当前线程的请求上下文，用ThreadLocal确保为当前对象
     */
    private final ThreadLocal<RequestContext> localContext = new ThreadLocal<>();

    /**
     * 系统所有的Action列表
     */
    private final List<Action> actions;

    @Inject
    public DefaultMikuDispatcher(MultipartConfigElement config) {
        List<Action> actions = new ArrayList<>();
        Set<Class<?>> classSet = ClassUtils.getClasses(me.constants().getControllerPath());
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
                MikuController controller = me.getInstance(controllerClass);
                controller.init();
                ControllerInfo controllerInfo = new ControllerInfo(controller);
                actions.addAll(controllerInfo.analyze());
            }
        }
        //// TODO: 2016/11/21
//        StaticAction staticAction = new StaticAction();
//        actions.add(staticAction);
        this.actions = actions;
        this.config = config;
    }

    /**
     * 调度器初始化
     */
    @Override
    public void init() {

    }

    /**
     * 调度分发客户端请求
     *
     * @param request  当前请求的 {@link HttpServletRequest}对象
     * @param response 当前请求的 {@link HttpServletResponse}对象
     */
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) {
        MikuRequest mikuRequest = new MikuRequest(request, config);
        RequestContext context = new DefaultRequestContext(mikuRequest, response, new DefaultModel());
        localContext.set(context);
        try {
            Render render = null;
            RouterInfo routerInfo = RouterInfo.create(context);
            List<Interceptor> interceptors = patternInterceptors(routerInfo);
            int size = interceptors.size();
            if (size > 0) {
                for (Interceptor interceptor : interceptors) {
                    if (!interceptor.preHandle(context.getRequest(), context.getResponse(), null)) {
                        return;
                    }
                }
            }
            for (Action action : actions) {
                render = action.matchAndInvoke(routerInfo);
                if (render != Render.NULL) {
                    break;
                }
            }
            if (render == Render.NULL) {
                StatusCodeRender.render404(context);
            }
            if (size > 0) {
                for (int i = size - 1; i >= 0; i--) {
                    interceptors.get(i).postHandle(context.getRequest(), context.getResponse(), null);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            StatusCodeRender.render405(context);
        } finally {
            localContext.remove();
        }
    }

    /**
     * 调度器销毁
     */
    @Override
    public void destroy() {

    }

    /**
     * @return 当前请求的 {@link HttpServletRequest}对象
     */
    @Override
    public HttpServletRequest currentRequest() {
        return currentRequestContext().getRequest();
    }

    /**
     * @return 当前请求的 {@link HttpServletResponse}对象
     */
    @Override
    public HttpServletResponse currentResponse() {
        return currentRequestContext().getResponse();
    }

    /**
     * @return 当前请求的 {@link RequestContext}对象
     */
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

    /**
     * 根据路径匹配拦截器
     *
     * @param routerInfo
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private List<Interceptor> patternInterceptors(RouterInfo routerInfo) throws IllegalAccessException, InstantiationException {
        List<InterceptorInfo> interceptorInfos = Miku.me.interceptors().getInterceptors();
        List<Interceptor> interceptors = new ArrayList<>();
        for (InterceptorInfo interceptorInfo : interceptorInfos) {
            List<String> mapping = interceptorInfo.getMapping();
            List<String> excludeMapping = interceptorInfo.getExcludeMapping();
            boolean flag = true;
            if (excludeMapping != null && excludeMapping.size() > 0) {
                for (String ss : excludeMapping) {
                    if (Pattern.matches(ss, routerInfo.getPath())) {
                        flag = false;
                        break;
                    }
                }
            }
            if (flag) {
                if (mapping != null && !mapping.isEmpty()) {
                    for (String s : mapping) {
                        if (Pattern.matches(s, routerInfo.getPath())) {
                            interceptors.add(interceptorInfo.getInterceptor().newInstance());
                            break;
                        }

                    }
                }
            }
        }
        return interceptors;
    }
}
