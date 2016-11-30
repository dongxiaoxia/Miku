package xyz.dongxiaoxia.miku.view;

import xyz.dongxiaoxia.miku.MikuException;
import xyz.dongxiaoxia.miku.context.RequestContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by 东小侠 on 2016/11/20.
 * 原生的PrintWriter渲染类，通过writer输出内容到页面上
 */
public class PrintWriterRender implements Render {

    /**
     * 当前请求的 {@link HttpServletResponse}对象
     */
    private final HttpServletResponse response;

    /**
     * 当前请求的 {@link HttpServletResponse}对象对应的Writer对象
     */
    private PrintWriter writer = null;

    private PrintWriterRender(HttpServletResponse response) {
        this.response = response;
        try {
            writer = response.getWriter();
        } catch (IOException e) {
            throw new MikuException(e.getMessage(), e);
        }
    }

    /**
     * 创建本类对象的静态类
     *
     * @param response 当前请求的 {@link HttpServletResponse}对象
     * @return
     */
    public static PrintWriterRender create(HttpServletResponse response) {
        return new PrintWriterRender(response);
    }

    private PrintWriter getWriter() {
        return writer;
    }

    public PrintWriterRender setStatus(int status) {
        response.setStatus(status);
        return this;
    }

    public PrintWriterRender write(String s) {
        writer.write(s);
        return this;
    }

    /**
     * 输出内容对页面
     *
     * @param requestContext 需要渲染的请求上下文
     */
    @Override
    public void render(RequestContext requestContext) {
        if (writer != null) {
            writer.flush();
            writer.close();
        }
    }
}
