package org.jeecg.task.track;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jeecg.HttpClientUtil;
import org.jeecg.WaybillNotifyStateEn;
import org.jeecg.WaybillStateEn;
import org.jeecg.en.WxMpTemplateMessageEnum;
import org.jeecg.modules.waybillInfo.entity.WaybillInfo;
import org.jeecg.modules.waybillInfo.entity.WaybillNoticeHistory;
import org.jeecg.modules.waybillInfo.mapper.WaybillInfoMapper;
import org.jeecg.modules.waybillInfo.mapper.WaybillNoticeHistoryMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: qiufeng
 * @Date: Created in 2021/3/18
 */

@Slf4j
@Component
public class ETH extends TrackAbstract {


    //物流跟踪
    @Override
    public void waybillTrack(WaybillInfo waybillInfo) {
        String html = post(waybillInfo.getWaybillNo());
        Document document = Jsoup.parse(html);
        Element mainContainer = document.body().getElementsByClass("container main-container").get(1);
        Element container = mainContainer.getElementsByClass("container").get(0);
        Element summary = container.getElementsByClass("table table-bordered table-striped").get(0);
        Element origin = summary.select("body > div.container.main-container > div.container > table:nth-child(3) > tbody > tr > td:nth-child(1)").get(0);
//        Element origin = document.select("body > div.container.main-container > div.container > table:nth-child(3) > tbody > tr > td:nth-child(1)").get(0);
    }


    public static void main(String[] args) {
        String html = post("071-40970635");
        Document document = Jsoup.parse(html);
        Element container = document.select("body > div.container.main-container > div.container").get(0);
        Element origin = container.select("table:nth-child(3) > tbody > tr > td:nth-child(1)").get(0);
        Element destination = container.select("table:nth-child(3) > tbody > tr > td:nth-child(2)").get(0);
        Element pieces = container.select("table:nth-child(3) > tbody > tr > td:nth-child(3)").get(0);
        Element volume = container.select("table:nth-child(3) > tbody > tr > td:nth-child(4)").get(0);
        Element airWaybillNumber = container.select("table:nth-child(3) > tbody > tr > td:nth-child(5)").get(0);
        Element weight = container.select("table:nth-child(3) > tbody > tr > td:nth-child(6)").get(0);
        Element detail = container.select("table:nth-child(4)").get(0);
        Element part1_origin = detail.select("tbody tr > td:nth-child(1)").get(0);
        Element part1_destination = detail.select("tbody tr > td:nth-child(2)").get(0);
        Element part1_flightNumber = detail.select("tbody tr > td:nth-child(3)").get(0);
        Element part1_flightDate = detail.select("tbody tr > td:nth-child(4)").get(0);
        Element part1_numberOfPieces = detail.select("tbody tr > td:nth-child(5)").get(0);
        Element part1_weight = detail.select("tbody tr > td:nth-child(6)").get(0);

        System.out.println("detail");
        Element detail_list = detail.select("tbody > tr:nth-child(2) > td:nth-child(2) > table > tbody").get(0);
        Elements list = detail_list.children();
        Collections.reverse(list);
        for (Element element : list) {
            Element active = element.select("td:nth-child(1)").get(0);
            Element activeDate = element.select("td:nth-child(2)").get(0);
            System.out.println(element.text());
        }

        Element child_detail = container.select("table:nth-child(5) > tbody").get(0);

        for (int child_detail_size = 2; child_detail_size <= child_detail.children().size(); child_detail_size = child_detail_size + 2) {
            Element child_list = child_detail.select("tr:nth-child(" + child_detail_size + ") > td:nth-child(2) > table > tbody").get(0);
            System.out.println("children_" + (child_detail_size - 1));
            Elements list_2 = child_list.children();
            Collections.reverse(list_2);
            for (Element element : list_2) {
                Element active = element.select("td:nth-child(1)").get(0);
                Element activeDate = element.select("td:nth-child(2)").get(0);
                System.out.println(element.text());
            }
        }
    }

    public static String post(String waybillNo) {
        String url = "https://cargo.ethiopianairlines.com/e-cargo/cargotrack/Index";
        String charset = "UTF-8";
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            httpClient = HttpClients.createDefault();
            //设置代理IP、端口
            httpPost = new HttpPost(url);
            httpPost.setHeader("Host", "cargo.ethiopianairlines.com");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            NameValuePair pair = new BasicNameValuePair("AirwayBilNum", waybillNo);
            params.add(pair);
            httpPost.setEntity(new UrlEncodedFormEntity(params, charset));
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
