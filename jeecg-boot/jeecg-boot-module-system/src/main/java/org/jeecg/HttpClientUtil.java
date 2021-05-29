package org.jeecg;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import okhttp3.*;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: qiufeng
 * @Date: Created in 2021/2/19
 */
public class HttpClientUtil {


    @SuppressWarnings("resource")
    public static String doPost(String url,String jsonstr,String charset){
        CloseableHttpClient  httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try{
            httpClient = HttpClients.createDefault();
            //设置代理IP、端口
            HttpHost proxy = new HttpHost("127.0.0.1",5555);
            RequestConfig requestConfig= RequestConfig.custom().setProxy(proxy).build();
            httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            NameValuePair pair = new BasicNameValuePair("AirwayBilNum", "071-40970635");
            params.add(pair);
            httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity,charset);
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return result;
    }


    public static void main(String[] args){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("AirwayBilNum","071-40970635");
        System.out.println(jsonObject.toJSONString());
        String str = doPost("http://cargo.ethiopianairlines.com/e-cargo/cargotrack/Index","","utf-8");

//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 5555)))
//                .build();
//        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
//        RequestBody body = RequestBody.create(mediaType, "AirwayBilNum=071-40970635&CheckIn=&CheckOut=");
//        Request request = new Request.Builder()
//                .url("http://cargo.ethiopianairlines.com/e-cargo/cargotrack/Index/")
//                .method("POST", body)
//                .addHeader("authority", "cargo.ethiopianairlines.com")
//                .addHeader("cache-control", "max-age=0")
//                .addHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"90\", \"Google Chrome\";v=\"90\"")
//                .addHeader("sec-ch-ua-mobile", "?0")
//                .addHeader("upgrade-insecure-requests", "1")
//                .addHeader("origin", "https://cargo.ethiopianairlines.com")
//                .addHeader("content-type", "application/x-www-form-urlencoded")
//                .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36")
//                .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
//                .addHeader("sec-fetch-site", "same-origin")
//                .addHeader("sec-fetch-mode", "navigate")
//                .addHeader("sec-fetch-user", "?1")
//                .addHeader("sec-fetch-dest", "document")
//                .addHeader("referer", "https://cargo.ethiopianairlines.com/e-cargo/cargotrack?awbnumber=071-40970635")
//                .addHeader("accept-language", "zh-CN,zh;q=0.9")
//                .addHeader("cookie", "_ga=GA1.2.1103181712.1620723707; _gid=GA1.2.672734468.1621870882; ASP.NET_SessionId=nafs5ij003utgqwpoh3upl0b")
//                .build();
//        try {
//            Response response = client.newCall(request).execute();
//            System.out.println("响应：");
//            System.out.println(response.body().string());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        System.out.println("响应：");
        System.out.println(str);
    }

    @SuppressWarnings("resource")
    public static String doGet(String url,String charset,String waybillNo){
        HttpClient httpClient = null;
        HttpGet httpGet = null;
        String result = null;
        try{
            // 定义请求的参数
            URI uri = new URIBuilder(url)
                    .setParameter("Carrier", "GF")
                    .setParameter("Shipment_text", waybillNo)
                    .setParameter("Header", "no")
                    .setParameter("Site", "GulfAir")
                    .build();
            httpClient = new SSLClient();
            httpGet = new HttpGet(uri);
            HttpResponse response = httpClient.execute(httpGet);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity,charset);
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return result;
    }
}

