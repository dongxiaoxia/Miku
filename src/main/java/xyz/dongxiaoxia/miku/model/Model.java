package xyz.dongxiaoxia.miku.model;

import com.google.inject.ImplementedBy;

import java.util.Map;

/**
 * Created by 东小侠 on 2016/11/19.
 * <p>
 * Model,MVC中的M,用于将Controller中的数据传给View
 */
@ImplementedBy(DefaultModel.class)
public interface Model {

    /**
     * 添加一个属性
     *
     * @param attributeName  属性名称
     * @param attributeValue 属性值
     * @return
     */
    Model add(String attributeName, Object attributeValue);

    /**
     * 根据属性名称获取属性值
     *
     * @param attributeName 属性名称
     * @return 对应的属性值
     */
    Object get(String attributeName);

    /**
     * @return 返回Model对应的map, 不会返回<code>null</code>
     */
    Map<String, Object> getModel();

    /**
     * 判断是否包含属性名称
     *
     * @param attributeName 属性名称
     * @return 是否包含属性名称
     */
    boolean contains(String attributeName);
}
