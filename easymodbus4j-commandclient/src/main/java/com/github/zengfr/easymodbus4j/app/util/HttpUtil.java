package com.github.zengfr.easymodbus4j.app.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by zengfr on 2020/11/26.
 */
public class HttpUtil {
    private static final CloseableHttpClient client ;
    private final static String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0";
    static {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(500);
        connectionManager.setDefaultMaxPerRoute(255);
        connectionManager.setValidateAfterInactivity(10000);

        client = HttpClients.custom().setConnectionManager(connectionManager).build();
    }
    public static String get(String urlString, String referer, String origin, String charest) throws IOException {
        HttpGet httpGet = new HttpGet(urlString);
        return exec( httpGet,  referer,  origin,  null,  charest);
    }
    public static String post(String urlString, String referer, String origin, String data, String charest) throws IOException {
        HttpPost post = new HttpPost(urlString);

        return exec( post,  referer,  origin,  data,  charest);
    }
    protected static String exec( HttpRequestBase req, String referer, String origin, String data, String charest) throws IOException {


        req.setHeader("User-Agent", userAgent);
        req.setHeader("Referer", referer);
        req.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + charest);
        req.setHeader("Sec-Fetch-Dest", "empty");
        req.setHeader("Sec-Fetch-Mode", "cors");
        req.setHeader("Sec-Fetch-Site", "same-origin");
        if (origin != null)
            req.setHeader("Origin", origin);
        if (data != null)
            ((HttpEntityEnclosingRequest)req).setEntity(new StringEntity(data));
        CloseableHttpResponse resp = client.execute(req);
        String content=getContent(resp, charest);

        EntityUtils.consume(resp.getEntity());
        resp.close();
        return content;
    }

    public static String getContent(HttpResponse resp, String charest) throws IOException {
        StringBuffer sb = new StringBuffer();
        HttpEntity entity = resp.getEntity();

        BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(), charest));
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            line = br.readLine();
        }

        return sb.toString();
    }
}
