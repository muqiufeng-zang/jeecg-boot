package org.jeecg.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;

/**
 * @Description:
 * @Author: qiufeng
 * @Date: Created in 2021/6/15
 */
public class Demo {

    public static void main(String[] args) throws IOException, InterruptedException {
        String API_KEY = "8c9a3282c16f7bc95a506619777e27fb";
        //下载的chromedriver位置
        System.setProperty("webdriver.chrome.driver", "/Users/zcp/Software/chromedriver");
        //实例化ChromeDriver对象
        WebDriver driver = new ChromeDriver();
        String url = "https://www.google.com/recaptcha/api2/demo";
        //打开指定网站
        driver.get(url);
        String data_sitekey = driver.findElement(new By.ByXPath("//*[@id=\"recaptcha-demo\"]")).getAttribute("data-sitekey");
        System.out.println("data_sitekey");
        System.out.println(data_sitekey);
        String page_url = "https://www.google.com/recaptcha/api2/demo";
        String u1 = "https://2captcha.com/in.php?key=" + API_KEY + "&method=userrecaptcha&googlekey=" + data_sitekey + "&pageurl=" + page_url + "&json=1&invisible=1";
        System.out.println("u1");
        System.out.println(u1);
        HttpGet httpGet = new HttpGet(u1);
        CloseableHttpClient httpClient = null;
        httpClient = HttpClients.createDefault();
        HttpResponse r1 = httpClient.execute(httpGet);
        String rid = JSONObject.parseObject(EntityUtils.toString(r1.getEntity(),"utf-8")).getString("request");
        System.out.println("rid");
        System.out.println(rid);
        String u2 = "https://2captcha.com/res.php?key=" + API_KEY + "&action=get&id=" + rid + "&json=1";
        Thread.sleep(40000);
        String form_tokon;
        while (true) {
            HttpGet httpGet2 = new HttpGet(u2);
            HttpResponse r2 = httpClient.execute(httpGet2);
            JSONObject r2Json = JSON.parseObject(EntityUtils.toString(r2.getEntity()));
            String status = r2Json.getString("status");
            if ("1".equals(status)) {
                form_tokon = r2Json.getString("request");
                break;
            }
            Thread.sleep(500);
        }
        String wirte_tokon_js = "document.getElementById(\"g-recaptcha-response\").innerHTML=\"" + form_tokon + "\";";
        String submit_js = "document.getElementById(\"recaptcha-demo-form\").submit();";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(wirte_tokon_js);
        Thread.sleep(500);
        js.executeScript(submit_js);
        Thread.sleep(5000);
        //解析页面
        String pageSource = driver.getPageSource();
        System.out.println(pageSource);
        driver.quit();
    }
}
