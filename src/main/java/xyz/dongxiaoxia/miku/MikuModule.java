package xyz.dongxiaoxia.miku;

import com.google.inject.AbstractModule;
import xyz.dongxiaoxia.miku.context.DefaultMultipartConfigElementProvider;

import javax.inject.Singleton;
import javax.servlet.MultipartConfigElement;

/**
 * Created by 01don on 2016/12/7.
 */
public class MikuModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MultipartConfigElement.class).toProvider(DefaultMultipartConfigElementProvider.class).in(Singleton.class);
    }
}
