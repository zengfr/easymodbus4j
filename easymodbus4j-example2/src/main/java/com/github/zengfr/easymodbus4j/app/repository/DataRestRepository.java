package com.github.zengfr.easymodbus4j.app.repository;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class DataRestRepository {
	private static final InternalLogger logger = InternalLoggerFactory.getInstance(DataRestRepository.class.getSimpleName());
	private static String serviceUrl = "http://120.127.126.144:19074";
	private static Cache<String, String> tokenCache = CacheBuilder.newBuilder()
			.expireAfterWrite(1000 * 60 * 10, TimeUnit.MILLISECONDS).build();

	private static String getTokenByCache() throws Exception {
		Callable<? extends String> loader = () -> {
			return getToken();
		};
		String token = tokenCache.get("token", loader);
		if (token == null || token.isEmpty()) {
			tokenCache.invalidateAll();
			// token =
			// "bearereyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJ3ZWJjbGllbnQiLCIgbW9iaWxlY2xpZW50Il0sInNob3BJZCI6ImNkODI2IiwiZXhwIjoxNTU4ODc4Nzc1LCJhdXRob3JpdGllcyI6WyJtb2RidXMiXSwianRpIjoiYzI3Y2MxMWMtOWQ5My00YTY1LWFiZmItNmEwNzA2OWJkZTAyIiwiY2xpZW50X2lkIjoiYXBpX2NsaWVudF90ZXN0In0.kwSanVJ6b4GKyQwjRFKVnIpLDeBYeSEi4dW22Q_vnxo";
		}
		return token;
	}

	private static String getToken() throws Exception {
		String url = "/oauth/token";
		access_tokenReq req = new access_tokenReq();
		req.client_id = "api_client_test";
		req.client_secret = "000000";
		req.grant_type = "client_credentials";

		access_tokenResp resp = post(String.format("%s%s?%s", serviceUrl, url, ""), req, false, access_tokenResp.class);
		if (resp != null && resp.access_token != null && !resp.access_token.isEmpty()) {
			return resp.token_type + resp.access_token;
		}
		return "";
	}

	public static mainboard_adressResp get_mainboard_addresslist() throws Exception {
		String url = "/api/modbus/param/mainboardadresslist?";
		req req = new req();
		req.access_token = getTokenByCache();
		return get(String.format("%s%s?%s", serviceUrl, url, ""), true, mainboard_adressResp.class);
	}

	public static autosend_listResp get_autosendlist(String versionId) throws Exception {
		String url = "/api/modbus/autosendlist";
		return get(String.format("%s%s?%s", serviceUrl, url, "model_no=" + versionId), true, autosend_listResp.class);
	}

	public static void update_values(update_modbus_valuesReq req) throws Exception {
		String url = "/api/modbus/values";
		req.access_token = getTokenByCache();
		post(String.format("%s%s?%s", serviceUrl, url, ""), req, true, req.class);
	}

	public static void update_ipport(update_slaveipportReq req) throws Exception {
		String url = "/api/modbus/slaveipport";
		req.access_token = getTokenByCache();
		post(String.format("%s%s?%s", serviceUrl, url, ""), req, true, req.class);
	}

	final static String CONTENT_TYPE_TEXT_JSON = "text/json";

	protected static <T, R> R post(String url, T body, boolean useToken, Class<R> clazz) throws Exception {
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type", "application/json;charset=UTF-8");
		if (useToken)
			post.addHeader("Authorization", getTokenByCache());
		post.setConfig(getRequestConfig());

		String bodyString = JSON.toJSONString(body);
		StringEntity se = new StringEntity(bodyString);
		se.setContentType(CONTENT_TYPE_TEXT_JSON);
		post.setEntity(se);
		DefaultHttpClient client = new DefaultHttpClient(new PoolingClientConnectionManager());
		CloseableHttpResponse response2 = null;

		response2 = client.execute(post);
		HttpEntity entity2 = response2.getEntity();
		String s2 = EntityUtils.toString(entity2, "UTF-8");
		logger.debug(String.format("post->url:%s;resp:%s", url, bodyString, s2));
		return JSON.parseObject(s2, clazz);
	}

	protected static <T, R> R get(String url, boolean useToken, Class<R> clazz) throws Exception {
		HttpGet get = new HttpGet(url);
		if (useToken)
			get.addHeader("Authorization", getTokenByCache());
		get.setConfig(getRequestConfig());

		DefaultHttpClient client = new DefaultHttpClient(new PoolingClientConnectionManager());
		CloseableHttpResponse response2 = null;

		response2 = client.execute(get);
		HttpEntity entity2 = response2.getEntity();
		String s2 = EntityUtils.toString(entity2, "UTF-8");
		logger.debug(String.format("get->url:%s;resp:%s", url, s2));
		return JSON.parseObject(s2, clazz);
	}

	protected static RequestConfig getRequestConfig() {
		RequestConfig requestConfig = null;
		if (requestConfig == null) {
			requestConfig = RequestConfig.custom().setConnectionRequestTimeout(20000).setConnectTimeout(20000)
					.setSocketTimeout(20000).build();
		}
		return requestConfig;
	}
}
