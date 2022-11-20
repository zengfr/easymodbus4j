package com.github.zengfr.easymodbus4j.app.gprs;

import com.alibaba.fastjson.JSON;
import com.github.zengfr.easymodbus4j.app.util.HttpUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zengfr on 2020/11/26.
 */
public class juheApi {
    public static class Resp {

        public String mcc;
        public String mnc;
        public String lac;
        public String ci;

        public String lat;
        public String lon;
        public String address;
        public String radius;

        public String rcontent;
    }

    private final static String api = "https://v.juhe.cn/cell/Triangulation/query.php";
    private final static String homePage = "https://www.juhe.cn/docs/api/id/8?bd_vid=7081628664865556502";

    public static void main(String... args) throws IOException {
        for (int i = 0; i < 10; i++) {
            test();
        }

    }

    public static void test() throws IOException {
        Resp resp = getGprs("4163", "21297934");
        System.out.println(JSON.toJSONString(resp));
    }

    public static Resp getGprs(String lac, String cId) throws IOException {
        return getGprs(lac, cId, false, "UTF-8");
    }

    public static Resp getGprs(String lac, String cId, boolean isHex, String charest) throws IOException {
        String data = String.format("mcc=460&mnc=0&lac=%s&ci=%s&hex=%s", lac, cId, isHex ? 16 : 10);
        String org = "https://v.juhe.cn";
        String contentString = HttpUtil.post(api, homePage, org, data, charest);

        String pat = "\"result\":(.*?)}";

        Matcher m = Pattern.compile(pat).matcher(contentString);
        String result;
        Resp resp = new Resp();
        if (m.find()) {
            result = m.group(1);
            if (result != null && !result.startsWith("null")) {
                resp = JSON.parseObject(result + "}", Resp.class);
            }
        }
        if (StringUtils.isEmpty(resp.address)) {
            resp.rcontent = contentString;
        }
        return resp;
    }


}
