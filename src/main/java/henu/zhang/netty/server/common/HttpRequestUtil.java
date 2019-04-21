package henu.zhang.netty.server.common;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import okhttp3.Request.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * @author wanglong
 * @date 15/8/4
 * http 请求工具类（包括post和get方法）
 */
@Service
public class HttpRequestUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpRequestUtil.class);

    OkHttpClient client = new OkHttpClient();

    /**
     * GET请求
     *
     * @param url
     * @param param
     * @return
     * @throws IOException
     */
    public String doGet(String url, Map<String, Object> param) throws IOException {

        if (param != null) {
            String paramLink = urlFix(param);
            url = url + "?" + paramLink;
        }

        Request request = new Builder().url(url).build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            logger.error("GET请求失败:{}",response);
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * POST请求
     *
     * @param url
     * @param param
     * @return
     * @throws IOException
     */
    public String doPost(String url, Map<String, Object> param) throws IOException {

        FormBody.Builder builder = new FormBody.Builder();

        for (String key : param.keySet()) {
            String value = String.valueOf(param.get(key));
            builder.add(key, value);
        }

        FormBody formBody = builder.build();

        Request request = new Builder()
                .url(url)
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            logger.error("GET请求失败:{}",response);
            throw new IOException("Unexpected code " + response);
        }

        return response.body().string();
    }

    /**
     * 请求参数为JSON形式的POST请求.
     *
     * @return
     * @throws IOException
     */
    public String doPostJSON(String url, Map<String, Object> param) throws IOException {

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody formBody = RequestBody.create(JSON, JSONObject.toJSONString(param));

        Request request = new Builder()
                .url(url)
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        return response.body().string();
    }


    //将Map转成可以拼接成url的String
    public String urlFix(Map<String, Object> paramMap) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();

        if (paramMap == null) {
            return "";
        } else {
            for (String key : paramMap.keySet()) {
                String value = String.valueOf(paramMap.get(key));
                if (sb.length() < 1) {
                    sb.append(key).append("=").append(URLEncoder.encode(value, "UTF-8"));
                } else {
                    sb.append("&").append(key).append("=").append(value);
                }
            }
            return sb.toString();
        }
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&amp;”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public String createLinkString(Map<String, Object> params) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        StringBuffer prestr = new StringBuffer();
        int size = keys.size() - 1;
        for (int i = 0; i < size; i++) {
            String key = keys.get(i);
            Object value = params.get(key);
            prestr.append(key).append("=").append(value).append("&");
        }
        String key = keys.get(size);
        Object value = params.get(key);
        prestr.append(key).append("=").append(value);//拼接时，不包括最后一个&字符
        return prestr.toString();
    }

}
