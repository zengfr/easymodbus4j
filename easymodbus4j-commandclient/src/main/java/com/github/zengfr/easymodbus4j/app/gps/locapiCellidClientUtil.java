package com.github.zengfr.easymodbus4j.app.gps;

import com.alibaba.fastjson.JSON;
import com.github.zengfr.easymodbus4j.app.util.HttpUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class locapiCellidClientUtil {
	public static class Resp {
		public String lat;
		public String lng;
		public String title;
	}

	private final static String api = "http://www.cellid.cn/cidInfo.php?lac=%s&cell_id=%s&hex=false&flag=%s";
	private final static String homePage = "http://www.cellid.cn/";

	public static void main(String... args) throws IOException {
		test();
	}

	public static void test() throws IOException {
		Resp resp = parseResp("13876", "1285714605", "UTF-8");
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

		String contentString = HttpUtil.get(urlString,homePage,homePage,charest);
		Matcher m = Pattern.compile("<input type=\"hidden\" id=\"flag\" name=\"flag\" value=\"(.*?)\">").matcher(contentString);
		if (m.find()) {
			return m.group(1);
		}
		return "";

	}
	protected static String parse(String lac, String cId, String flag, String charest) throws IOException {
		String urlString = String.format(api, lac, cId, flag);
		String  content=HttpUtil.post (urlString,homePage,null,null,charest);
		return content;
	}

}
