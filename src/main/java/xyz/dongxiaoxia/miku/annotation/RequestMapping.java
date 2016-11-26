package xyz.dongxiaoxia.miku.annotation;

import java.lang.annotation.*;

/**
 * Created by 东小侠 on 2016/11/23.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

    String value() default "";

    RequestMethod method() default RequestMethod.ALL;
}
