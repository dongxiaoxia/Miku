package xyz.dongxiaoxia.miku.context;

import xyz.dongxiaoxia.miku.Miku;
import xyz.dongxiaoxia.miku.MikuException;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.io.IOException;

/**
 * Created by 01don on 2016/12/7.
 */
@Singleton
public class DefaultMultipartConfigElementProvider implements Provider<MultipartConfigElement> {

    private final MultipartConfigElement config;

    @Inject
    public DefaultMultipartConfigElementProvider() {
        try {
            File tempDir = (File) Miku.me.servletContext().getAttribute("javax.servlet.context.tempdir");
            config = new MultipartConfigElement(tempDir.getCanonicalPath());
        } catch (IOException e) {
            throw new MikuException(e.getMessage(), e);
        }
    }

    @Override
    public MultipartConfigElement get() {
        return config;
    }
}
