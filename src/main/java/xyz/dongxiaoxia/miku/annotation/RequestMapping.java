package xyz.dongxiaoxia.miku.annotation;

import java.lang.annotation.*;

/**
 * Created by 东小侠 on 2016/11/23.
 * <p>
 * RequestMapping注解，标注在类上或方法上作为Action
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

    /**
     * @return Action路径
     */
    String value() default "";

    /**
     * @return 请求方法
     */
    RequestMethod method() default RequestMethod.ALL;
}
