package xyz.dongxiaoxia.miku.view;

import xyz.dongxiaoxia.miku.MikuException;
import xyz.dongxiaoxia.miku.context.RequestContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by 东小侠 on 2016/11/20.
 */
public class PrintWriterRender implements Render {

    private final HttpServletResponse response;
    private PrintWriter writer = null;

    private PrintWriterRender(HttpServletResponse response) {
        this.response = response;
        try {
            writer = response.getWriter();
        } catch (IOException e) {
            throw new MikuException(e.getMessage(),e);
        }
    }

    public static PrintWriterRender create(HttpServletResponse response){
        return new PrintWriterRender(response);
    }

    private PrintWriter getWriter(){
       return writer;
    }

    public PrintWriterRender setStatus(int status){
        response.setStatus(status);
        return this;
    }

    public PrintWriterRender write(String s){
        writer.write(s);
        return  this;
    }

    @Override
    public void render(RequestContext requestContext) {
        if (writer !=null){
            writer.flush();
            writer.close();
        }
    }
}
