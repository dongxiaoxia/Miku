package xyz.dongxiaoxia.miku.controller;

import xyz.dongxiaoxia.miku.MikuException;
import xyz.dongxiaoxia.miku.annotation.RequestMapping;
import xyz.dongxiaoxia.miku.annotation.RequestMethod;
import xyz.dongxiaoxia.miku.context.RequestContext;
import xyz.dongxiaoxia.miku.model.Model;
import xyz.dongxiaoxia.miku.view.Render;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by 东小侠 on 2016/11/19.
 */
public class MethodAction implements Action {

    private final ControllerInfo controllerInfo;
    private final Method method;
    private final RequestMethod requestMethod;
    private final String pathPattern;

    private final List<String> paramName;
    private final List<Class<?>> paramTypes;
    private final Set<Annotation> annotations;

    private final boolean isPattern;

    public MethodAction(ControllerInfo controllerInfo, Method method) {
        this.controllerInfo = controllerInfo;
        this.method = method;
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String controllerPattern = controllerInfo.getPathUrl();
        String originPathPattern = controllerPattern + requestMapping.value();
        if (originPathPattern.length() > 1 && originPathPattern.endsWith("/")) {
            originPathPattern = originPathPattern.substring(0, originPathPattern.length() - 2);
        }
        this.pathPattern = originPathPattern;
        this.paramTypes = Arrays.asList(method.getParameterTypes());
        this.paramName = null;
        this.requestMethod = requestMapping.method();
        this.annotations = new HashSet<>(Arrays.asList(method.getAnnotations()));
        this.isPattern = false;
    }

    @Override
    public Render matchAndInvoke(RouterInfo routerInfo) {
        if (RequestMethod.GET.equals(this.requestMethod) && !routerInfo.getRequestMethod().equals(RequestMethod.GET)){
                return null;
        }
        if (RequestMethod.POST.equals(this.requestMethod) && !routerInfo.getRequestMethod().equals(RequestMethod.POST)){
            return null;
        }
        String path = "".equals(routerInfo.getPath()) ? routerInfo.getSimplePath() : routerInfo.getPath();
        if (!Pattern.compile(pathPattern).matcher(path).matches()) return null;
        int size = paramTypes.size();
        Object[] param = new Object[paramTypes.size()];
        RequestContext context = routerInfo.getRequestContext();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                if (HttpServletRequest.class.equals(paramTypes.get(i))) {
                    param[i] = context.getRequest();
                } else if (HttpServletResponse.class.equals(paramTypes.get(i))) {
                    param[i] = context.getResponse();
                } else if (Model.class.equals(paramTypes.get(i))) {
                    param[i] = context.getModel();
                }
            }
        }
        try {
            Object result = method.invoke(controllerInfo.getController(), param);
            Model model = context.getModel();
            if (model != null && !model.getModel().isEmpty()) {
                Set<Map.Entry<String, Object>> keySet = model.getModel().entrySet();
                for (Map.Entry<String, Object> entry : keySet) {
                    context.getRequest().setAttribute(entry.getKey(), entry.getValue());
                }
            }
            Render render = Render.class.cast(result);
            render.render(context);
            return render;
        } catch (Exception e) {
            throw new MikuException(e.getMessage(), e);
        }
    }
}
