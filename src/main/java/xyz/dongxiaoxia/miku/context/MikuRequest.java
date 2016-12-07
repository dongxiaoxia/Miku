package xyz.dongxiaoxia.miku.context;

import org.eclipse.jetty.util.MultiException;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.MultiPartInputStreamParser;
import org.eclipse.jetty.util.UrlEncoded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.dongxiaoxia.json.JackSonParseTool;
import xyz.dongxiaoxia.miku.MikuException;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by 东小侠 on 2016/12/1.
 * <p>
 * 封装HttpServletRequest请求，包括参数处理、文件上传等方便客户端使用的方法
 */
public class MikuRequest extends HttpServletRequestWrapper implements Closeable {

    private static Logger logger = LoggerFactory.getLogger(MikuRequest.class);

    //要封装的 {@link HttpServletRequest}对象
    private final MultipartConfigElement config;
    //上传文件对应的管理配置
    private final HttpServletRequest request;

    //url上的查询字符串对应的集合
    private Map<String, Collection<String>> queryStrings = null;
    //表单形式提交的参数集合
    private Map<String, Collection<String>> forms = new HashMap<>();
    //queryStrings + forms,forms内容会覆盖queryStrings
    private Map<String, Collection<String>> params = new HashMap<>();
    //上传文件的集合，key为文件名
    private Map<String, Collection<Part>> partMap = new HashMap<>();
    //上传文件的Set集合
    private Collection<Part> parts = new HashSet<>();
    //application/json形式提交时body内容转换的集合
    private Map<String, Object> jsonParams;

    private MultiPartInputStreamParser multiParser;

    private static int maxFormKeys = 1000; //最大的key数量
    private static int maxFormContentSize = 200000;    //post form数据的最大尺寸，jetty 200K, tomcat 2M

    /**
     * 根据给定的request构建转换为MikuRequest
     *
     * @param request 要封装的 {@link HttpServletRequest}对象
     * @param config  上传文件对应的管理配置
     * @throws IllegalArgumentException if the request is null
     */
    public MikuRequest(HttpServletRequest request, MultipartConfigElement config) {
        super(request);
        this.request = request;
        this.config = config;
        //处理请求数据
        parseParam();
    }

    /**
     * 根据参数名称获取参数，包括url与body的参数
     *
     * @param name 参数名称
     * @return 参数名称对应的参数值
     * @see ServletRequest#getParameter(String)
     */
    @Override
    public String getParameter(String name) {
        return getParamsString(params, name, false);
    }

    /**
     * @return url与body所有的参数集合
     * @see ServletRequest#getParameterMap()
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = new HashMap<>();
        for (Map.Entry<String, Collection<String>> entry : params.entrySet()) {
            map.put(entry.getKey(), entry.getValue().toArray(new String[entry.getValue().size()]));
        }
        return map;
    }

    /**
     * @return url与body所有的参数名称集合
     * @see ServletRequest#getAttributeNames()
     */
    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(params.keySet());
    }

    /**
     * @param name 参数名称
     * @return 参数名称对应的参数值的数组
     * @see ServletRequest#getParameterValues(String)
     */
    @Override
    public String[] getParameterValues(String name) {
        Collection<String> vs = params.get(name);
        return vs == null ? new String[0] : vs.toArray(new String[vs.size()]);
    }

    /**
     * @return url的查询字符串对应的集合
     */
    public Map<String, Collection<String>> queryStrings() {
        return queryStrings;
    }

    /**
     * 获取url上查询参数值
     *
     * @param name 参数名称
     * @return url上查询参数值
     */
    public String queryString(String name) {
        return getParamsString(queryStrings, name, true);
    }

    /**
     * @return 表单形式提交的参数集合
     */
    public Map<String, Collection<String>> forms() {
        return forms;
    }

    /**
     * 获取表单形式提交的参数
     *
     * @param name 参数名称
     * @return 参数名称对应的参数值
     */
    public String form(String name) {
        return getParamsString(forms, name, true);
    }

    /**
     * 根据参数名称获取参数集合
     *
     * @param params      参数集合
     * @param name        参数名称
     * @param nullToEmpty null是否要转换为""
     * @return 参数名称对应的参数值
     */
    private String getParamsString(Map<String, Collection<String>> params, String name, boolean nullToEmpty) {
        Collection<String> ps = params.get(name);
        return (ps == null || ps.size() == 0) ? nullToEmpty ? "" : null : ps.iterator().next();
    }

    /**
     * 获取application/json形式提交的参数内容
     *
     * @return json形式提交的参数内容
     */
    public Map<String, Object> jsonParams() {
        return jsonParams;
    }

    /**
     * @return 上传文件的集合
     */
    @Override
    public Collection<Part> getParts() {
        return parts;
    }

    /**
     * 根基文件名称获取上传文件
     *
     * @param name 文件名称
     * @return 文件名称对应的文件
     */
    @Override
    public Part getPart(String name) {
        Collection<Part> parts = partMap.get(name);
        return parts == null ? null : parts.size() == 0 ? null : parts.iterator().next();
    }

    /**
     * 处理请求数据
     */
    private void parseParam() {
        //获取url上的查询参数，无论是哪种请求方法
        MultiMap<String> params = new MultiMap<>();
        String originQueryString = super.getQueryString();
        if (originQueryString != null) {
            UrlEncoded.decodeTo(originQueryString, params, "UTF-8");
            queryStrings = transform(params);
        }
        //获取body参数
        if (!"get".equals(request.getMethod().toLowerCase())) {
            String contentType = request.getContentType();
            try {
                if (contentType == null) {
                    parsePostUrlEncoded();
                } else {
                    if (contentType.toLowerCase().startsWith("multipart/")) {
                        parsePostMulti();
                    } else if (contentType.toLowerCase().startsWith("application/json")) {
                        parsePostJson();
                    } else {
                        parsePostUrlEncoded();
                    }
                }
            } catch (Exception e) {
                throw new MikuException(e.getMessage(), e);
            }
        }
    }

    /**
     * 处理文件上传类型请求数据
     *
     * @throws IOException
     */
    private void parsePostMulti() throws IOException {
        InputStream in = new BufferedInputStream(super.getInputStream());
        String contentType = super.getContentType();
        File tempDir = new File(config.getLocation());
        multiParser = new MultiPartInputStreamParser(in, contentType, config, tempDir);
        multiParser.setDeleteOnExit(true);
        Collection<Part> parts = multiParser.getParts();
        int keyCount = queryStrings == null ? 0 : queryStrings.size();

        MultiMap<String> formMultiMap = new MultiMap<>();
        MultiMap<Part> partMultiMap = new MultiMap<>();
        Set<Part> partSet = new HashSet<>();

        Iterator<Part> iterator = parts.iterator();
        while (iterator.hasNext() && (keyCount++) < maxFormKeys) {
            Part part = iterator.next();
            if (part == null) {
                continue;
            }
            MultiPartInputStreamParser.MultiPart mp = (MultiPartInputStreamParser.MultiPart) part;
            if (mp.getFile() != null) {
                partMultiMap.put(part.getName(), part);
                partSet.add(part);
            } else {
                InputStream inputStream = part.getInputStream();
                byte[] bytes = new byte[inputStream.available()];
                part.getInputStream().read(bytes);
                String value = new String(bytes, Charset.forName("UTF-8"));
                formMultiMap.put(part.getName(), value);

            }
        }
        this.forms = transform(formMultiMap);
        this.params.putAll(transform(formMultiMap));
        this.partMap = transform(partMultiMap);
        this.parts = partSet;
    }

    /**
     * 处理json数据
     */
    private void parsePostJson() {
        try {
            super.setCharacterEncoding("UTF-8");
            BufferedReader br = super.getReader();
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String jsonStr = sb.toString();
            jsonParams = JackSonParseTool.parse2HashMap(jsonStr, String.class, Object.class);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            jsonParams = null;
        }
    }

    /**
     * 处理application/x-www-form-urlencoded类型数据
     */
    private void parsePostUrlEncoded() throws IOException {
        MultiMap<String> params = new MultiMap<>();
        UrlEncoded.decodeTo(super.getInputStream(), params, "UTF-8", maxFormContentSize, maxFormKeys);
        forms = transform(params);
        Map<String, Collection<String>> map = new HashMap<>();
        if (queryStrings != null) {
            map.putAll(queryStrings);
        }
        if (forms != null) {
            map.putAll(forms);
        }
        this.params = map;
    }

    /**
     * 清除上传的临时文件
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        if (multiParser != null)
            try {
                multiParser.deleteParts();
            } catch (MultiException e) {
                logger.error(e.getMessage(), e);
            }
    }

    /**
     * 集合转换
     */
    private <T> Map<String, Collection<T>> transform(MultiMap<T> multiMap) {
        Map<String, Collection<T>> map = new HashMap<>();
        if (multiMap == null || multiMap.isEmpty()) {
            return map;
        }
        for (MultiMap.Entry<String, List<T>> entry : multiMap.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }
}
