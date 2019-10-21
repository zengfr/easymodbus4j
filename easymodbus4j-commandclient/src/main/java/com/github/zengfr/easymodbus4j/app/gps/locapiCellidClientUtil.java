package com.github.zengfr.easymodbus4j.app.gps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

import com.alibaba.fastjson.JSON;

public class locapiCellidClientUtil {
	public static class Resp {
		public String lat;
		public String lng;
		public String title;
	}

	private final static String api = "http://www.cellid.cn/cidInfo.php?lac=%s&cell_id=%s&hex=false&flag=%s";
	private final static String homePage = "http://www.cellid.cn/";
	private static HttpClient client = HttpClientBuilder.create().build();

	public static void main(String... args) throws IOException {
		test();
	}

	public static void test() throws IOException {
		Resp resp = parseResp("4163", "21297934", "UTF-8");
		System.out.println(JSON.toJSONString(resp));
	}

	public static Resp parseResp(String lac, String cId, String charest) throws IOException {
		// cidMap(39.941548,116.352661,'(4163,21297934)
		// 39.941548,116.352661<br>北京市西城区西外大街1号')
		String flag = getFlag(charest);
		String contentString = parse(lac, cId, flag, charest);
		Resp resp = new Resp();
		String pat = "\\((.*?),(.*?),'(.*?)<br>(.*?)'";
		Matcher m = Pattern.compile(pat).matcher(contentString);
		if (m.find()) {
			resp.lat = m.group(1);
			resp.lng = m.group(2);
			resp.title = m.group(4);
		}

		return resp;
	}

	protected static String getFlag(String charest) throws ClientProtocolException, IOException {
		String urlString = homePage;
		HttpGet httpGet = new HttpGet(urlString);
		HttpResponse resp = client.execute(httpGet);
		String contentString = getContent(resp,charest);
		Matcher m = Pattern.compile("<input type=\"hidden\" id=\"flag\" name=\"flag\" value=\"(.*?)\">").matcher(contentString);
		if (m.find()) {
			return m.group(1);
		}
		return "";

	}

	protected static String parse(String lac, String cId, String flag, String charest) throws IOException {
		String urlString = String.format(api, lac, cId, flag);
		HttpPost post = new HttpPost(urlString);
		post.setHeader("Referer", homePage);
		post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");
		HttpResponse resp = client.execute(post);
		return getContent(resp, charest);
	}

	protected static String getContent(HttpResponse resp, String charest) throws IOException {
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
