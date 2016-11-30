package xyz.dongxiaoxia.miku.annotation;

import java.lang.annotation.*;

/**
 * Created by 东小侠 on 2016/11/26.
 * <p>
 * Controller注解，标记一个类为Controller
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {
}
