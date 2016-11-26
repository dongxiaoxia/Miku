package xyz.dongxiaoxia.miku.model;

import com.google.inject.ImplementedBy;

import java.util.Map;

/**
 * Created by 东小侠 on 2016/11/19.
 */
@ImplementedBy(DefaultModel.class)
public interface Model {
    Model add(String attributeName, Object attributeValue);

    Object get(String attributeName);

    Map<String,Object> getModel();

    boolean contains(String attributeName);
}
