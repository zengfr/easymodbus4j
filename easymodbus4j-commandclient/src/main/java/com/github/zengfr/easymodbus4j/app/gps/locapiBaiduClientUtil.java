package com.github.zengfr.easymodbus4j.app.gps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

/**
 * 智能硬件定位 可以通过蓝牙、WI-FI等获取用户定位数据 利用蓝牙、WI-FI等信息，传给服务端进行处理，获取定位信息，完成地图、路线规划、轨迹等功能
 * http://lbsyun.baidu.com/index.php?title=webapi/intel-hardware-api
 * https://api.map.baidu.com/locapi/v2 根据mcc, mnc,lac,cellid访问百度接口 -gprs移动定位
 * 适用于室内、室外多种定位场景，覆盖智能可穿戴设备、车载设备等。
 */
public class locapiBaiduClientUtil {
	private static String api = "https://api.map.baidu.com/locapi/v2";
	private static String apiKey = "z4Xg8Va4P2u1on5kjrU6ATGn2niaELgg";

	public static void main(String... args) throws IOException {
		test();
	}

	public static void test() throws IOException {

		String bts = "460,0,4163,21297934,-124";
		parse(bts);

		gprsData r = new gprsData();
		r.MCC = 0;
		r.MNC = 0;
		r.LAC = 0;
		r.CID = 0;
		r.signal = -1;
		parse(r);

	}

	public static locapiRespBody parse(gprsData r) throws IOException {
		String bts = String.format("%s,%s,%s,%s,%s", r.MCC, r.MNC, r.LAC, r.CID, r.signal);
		return parse(bts);
	}

	public static locapiRespBody parse(String bts) throws IOException {

		locapiReqBody body = new locapiReqBody();

		body.setAccesstype(0);// 移动接入网络：0 wifi接入网络：1 仅gps坐标转换：2
		body.setCdma(0);// 非cdma：0; cdma：1 默认值为：0
		body.setNetwork("GPRS");// 无线网络类型 GSM/GPRS/EDGE/HSUPA/HSDPA/WCDMA (注意大写)
		body.setImei("18701966811");
		body.setBts(bts);
		//非CDMA格式为：mcc, mnc,lac,cellid,signal 
        //CDMA格式为：sid,nid,bid,lon,lat,signal 其中lon,lat可为空，格式为：sid,nid,bid,,,signal
		body.setNeed_rgc("Y");// 返回地址信息，默认不返回 Y : 返回rgc结果 N : 不返回rgc结果
		body.setCtime("" + LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
		body.setOutput("JSON");

		locapiReq req = new locapiReq();
		req.setVer("1.0");
		req.setTrace(false);
		req.setSrc("bdl");
		req.setProd("bdl");
		req.setKey(apiKey);
		req.setBody(Lists.newArrayList(body));

		return parse(req);
	}

	protected static locapiRespBody parse(locapiReq req) throws IOException {
		StringBuffer sb = new StringBuffer();
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(api);
		String reqStr = JSON.toJSONString(req);
		StringEntity se = new StringEntity(reqStr);
		System.out.println(reqStr);
		post.setEntity(se);
		HttpResponse resp = client.execute(post);
		HttpEntity entity = resp.getEntity();
		BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
		String line = br.readLine();

		while (line != null) {
			sb.append(line);
			line = br.readLine();
		}
		System.out.println(sb);//740 Failed to authenticate for api loc is forbidden.（服务被禁用，一般不会出现）
		return JSON.parseObject(sb.toString(), locapiRespBody.class);
	}
}
