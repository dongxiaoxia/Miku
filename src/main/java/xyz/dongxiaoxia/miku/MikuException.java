package xyz.dongxiaoxia.miku;

/**
 * Created by 东小侠 on 2016/11/25.
 */
public class MikuException extends RuntimeException {

    public MikuException() {
        super();
    }

    public MikuException(String message) {
        super(message);
    }


    public MikuException(String message, Throwable cause) {
        super(message, cause);
    }

    public MikuException(Throwable cause) {
        super(cause);
    }
}
