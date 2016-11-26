package xyz.dongxiaoxia.miku.controller;

import xyz.dongxiaoxia.miku.annotation.RequestMapping;
import xyz.dongxiaoxia.miku.annotation.RequestMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by 东小侠 on 2016/11/19.
 */
public class ControllerInfo {
    private final MikuController controller;
    private final Class<? extends MikuController> clazz;
    private RequestMapping requestMapping;
    private RequestMethod requestMethod;
    private final String pathUrl;
    private final Set<Annotation> annotations;

    public ControllerInfo(MikuController mikuController) {
        this.controller = mikuController;
        clazz = controller.getClass();
        if (clazz.isAnnotationPresent(RequestMapping.class)) {
            this.requestMapping = clazz.getAnnotation(RequestMapping.class);
            this.requestMethod = requestMapping.method();
        } else {
            this.requestMapping = null;
            this.requestMethod = RequestMethod.ALL;
        }
        this.annotations = new HashSet<>(Arrays.asList(clazz.getAnnotations()));
        String pathUrl = requestMapping == null ? "" : requestMapping.value();
        if (pathUrl.length() == 0 || pathUrl.charAt(0) != '/') {
            pathUrl = '/' + pathUrl;
        }
        this.pathUrl = pathUrl;
    }

    public List<Action> analyze() {
        List<Action> actions = new ArrayList<>();
        Set<Method> methods = new HashSet<>(Arrays.asList(clazz.getDeclaredMethods()));
        actions.addAll(methods.stream().filter(method -> method.isAnnotationPresent(RequestMapping.class)).map(method -> new MethodAction(this, method)).collect(Collectors.toList()));
        return actions;
    }

    public MikuController getController() {
        return controller;
    }

    public Class<? extends MikuController> getClazz() {
        return clazz;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public String getPathUrl() {
        return pathUrl;
    }

    public Set<Annotation> getAnnotations() {
        return annotations;
    }
}
