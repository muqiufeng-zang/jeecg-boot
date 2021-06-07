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
//            httpPost.setConfig(requestConfig);
//            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("Host","cargo.ethiopianairlines.com");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            NameValuePair pair = new BasicNameValuePair("AirwayBilNum", "071-40970635");
            params.add(pair);
            httpPost.setEntity(new UrlEncodedFormEntity(params,charset));
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
        String str = doPost("https://cargo.ethiopianairlines.com/e-cargo/cargotrack/Index","","utf-8");
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

