package xyz.dongxiaoxia.miku.controller;

import xyz.dongxiaoxia.miku.MikuException;
import xyz.dongxiaoxia.miku.annotation.RequestMapping;
import xyz.dongxiaoxia.miku.annotation.RequestMethod;
import xyz.dongxiaoxia.miku.context.MikuRequest;
import xyz.dongxiaoxia.miku.context.RequestContext;
import xyz.dongxiaoxia.miku.model.Model;
import xyz.dongxiaoxia.miku.view.Render;

import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by 东小侠 on 2016/11/19.
 * <p>
 * Controller下面的方法对应的处理request客户端请求的封装对象
 */
public class MethodAction implements Action {

    /**
     * Action对应的 {@link ControllerInfo}对象
     */
    private final ControllerInfo controllerInfo;

    /**
     * 对象的方法
     */
    private final Method method;

    /**
     * 对应的request请求方法
     */
    private final RequestMethod requestMethod;

    /**
     * 路径正则表达式
     */
    private final String pathPattern;

    /**
     * 对应的方法参数名称列表，按顺序排序
     */
    private final List<String> paramName;

    /**
     * 对应的方法参数类型列表，按顺序排序
     */
    private final List<Class<?>> paramTypes;

    /**
     * 对应方法上的所有注解
     */
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

    /**
     * 匹配路由信息并且处理请求
     *
     * @param routerInfo 当前请求的路由信息
     * @return 匹配或指向处理请求结果
     */
    @Override
    public Render matchAndInvoke(RouterInfo routerInfo) {
        if (RequestMethod.GET.equals(this.requestMethod) && !routerInfo.getRequestMethod().equals(RequestMethod.GET)) {
            return Render.NULL;
        }
        if (RequestMethod.POST.equals(this.requestMethod) && !routerInfo.getRequestMethod().equals(RequestMethod.POST)) {
            return Render.NULL;
        }
        String path = "".equals(routerInfo.getPath()) ? routerInfo.getSimplePath() : routerInfo.getPath();
        if (!Pattern.compile(pathPattern).matcher(path).matches()) return null;
        int size = paramTypes.size();
        Object[] param = new Object[paramTypes.size()];
        RequestContext context = routerInfo.getRequestContext();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                if (paramTypes.get(i).isAssignableFrom(MikuRequest.class)) {
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
