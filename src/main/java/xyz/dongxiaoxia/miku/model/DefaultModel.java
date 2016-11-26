package xyz.dongxiaoxia.miku.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 东小侠 on 2016/11/19.
 */
public class DefaultModel implements Model {

    private Map<String,Object> data = new HashMap<>();

    @Override
    public Model add(String attributeName, Object attributeValue) {
        data.put(attributeName,attributeValue);
        return this;
    }

    @Override
    public Object get(String attributeName) {
        return data.get(attributeName);
    }

    @Override
    public Map<String, Object> getModel() {
        return data;
    }

    @Override
    public boolean contains(String attributeName) {
        return data.containsKey(attributeName);
    }
}
