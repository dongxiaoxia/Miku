package xyz.dongxiaoxia.miku.config;

/**
 * Created by 东小侠 on 2016/11/26.
 * <p>
 * 框架常量
 */
public class Constants {

    private boolean devMode = false; //debug模式，默认不开启
    private String encoding = "UTF-8";//编码格式
    private String viewSuffix = ".jsp";//视图后缀
    private String viewPrefix = "/WEB-INF/";//视图前缀
    private String controllerPath = "";//扫描Controller的包路径

    public boolean isDevMode() {
        return devMode;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getViewSuffix() {
        return viewSuffix;
    }

    public void setViewSuffix(String viewSuffix) {
        this.viewSuffix = viewSuffix;
    }

    public String getViewPrefix() {
        return viewPrefix;
    }

    public void setViewPrefix(String viewPrefix) {
        this.viewPrefix = viewPrefix;
    }

    public String getControllerPath() {
        return controllerPath;
    }

    public void setControllerPath(String controllerPath) {
        this.controllerPath = controllerPath;
    }
}
