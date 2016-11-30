package xyz.dongxiaoxia.miku.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 东小侠 on 2016/11/19.
 * <p>
 * * Model,MVC中的M,用于将Controller中的数据传给View
 */
public class DefaultModel implements Model {

    private Map<String, Object> data = new HashMap<>();

    /**
     * 添加一个属性
     *
     * @param attributeName  属性名称
     * @param attributeValue 属性值
     * @return
     */
    @Override
    public Model add(String attributeName, Object attributeValue) {
        data.put(attributeName, attributeValue);
        return this;
    }

    /**
     * 根据属性名称获取属性值
     *
     * @param attributeName 属性名称
     * @return 对应的属性值
     */
    @Override
    public Object get(String attributeName) {
        return data.get(attributeName);
    }

    /**
     * @return 返回Model对应的map, 不会返回<code>null</code>
     */
    @Override
    public Map<String, Object> getModel() {
        return data;
    }

    /**
     * 判断是否包含属性名称
     *
     * @param attributeName 属性名称
     * @return 是否包含属性名称
     */
    @Override
    public boolean contains(String attributeName) {
        return data.containsKey(attributeName);
    }
}
