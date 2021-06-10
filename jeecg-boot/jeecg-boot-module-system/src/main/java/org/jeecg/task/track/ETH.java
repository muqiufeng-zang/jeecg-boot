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
import org.jeecg.notify.MessageNotify;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Resource
    private WaybillNotice waybillNotice;

    @Resource
    private WaybillInfoMapper waybillInfoMapper;

    //物流跟踪
    @Override
    public void waybillTrack(WaybillInfo waybillInfo) {
        String html = post(waybillInfo.getWaybillNo());
        Document document = Jsoup.parse(html);
        Element container = document.select("body > div.container.main-container > div.container").get(0);
        Element origin = container.select("> table:nth-child(3) > tbody > tr > td:nth-child(1)").get(0);
        Element destination = container.select("> table:nth-child(3) > tbody > tr > td:nth-child(2)").get(0);
        Element pieces = container.select("> table:nth-child(3) > tbody > tr > td:nth-child(3)").get(0);
        Element volume = container.select("> table:nth-child(3) > tbody > tr > td:nth-child(4)").get(0);
        Element airWaybillNumber = container.select("> table:nth-child(3) > tbody > tr > td:nth-child(5)").get(0);
        if (!waybillInfo.getWaybillNo().equals(airWaybillNumber.text())) {
            log.warn("运单【{}】查询出来的信息不一致,查询到的是【{}】，请尽快核实！", waybillInfo.getWaybillNo(), airWaybillNumber.text());
            return;
        }
        Element weight = container.select("> table:nth-child(3) > tbody > tr > td:nth-child(6)").get(0);
        Element detail = container.select("> table:nth-child(4)").get(0);
        Element part1_origin = detail.select("> tbody tr > td:nth-child(2)").get(0);
        Element part1_destination = detail.select("> tbody tr > td:nth-child(3)").get(0);
        Element part1_flightNumber = detail.select("> tbody tr > td:nth-child(4)").get(0);
        Element part1_flightDate = detail.select("> tbody tr > td:nth-child(5)").get(0);
        Element part1_numberOfPieces = detail.select("> tbody tr > td:nth-child(6)").get(0);
        Element part1_weight = detail.select("> tbody tr > td:nth-child(7)").get(0);

        waybillInfo.setPieces(Integer.valueOf(pieces.text()));
        waybillInfo.setDestination(destination.text());
        waybillInfo.setOrigin(origin.text());
        waybillInfo.setVolume(volume.text());
        waybillInfo.setWeight(weight.text());
//        Integer waybillStateCode = WaybillStateEn.toEnum(waybillState).getCode();
//        waybillInfo.setWaybillSate(waybillStateCode);
        waybillInfoMapper.updateById(waybillInfo);

        System.out.println("detail");
        Element detail_list = detail.select("> tbody > tr:nth-child(2) > td:nth-child(2) > table > tbody").get(0);
        Elements list = detail_list.children();
        Collections.reverse(list);
        for (Element element : list) {
            Element active = element.select("> td:nth-child(1)").get(0);
            Element activeDate = element.select("> td:nth-child(2)").get(0);
            System.out.println(element.text());
            WaybillNoticeHistory waybillNoticeHistory = new WaybillNoticeHistory();
            waybillNoticeHistory.setFlightNumber(part1_flightNumber.text());
//            waybillNoticeHistory.setStatus();
            waybillNoticeHistory.setNotifyData(activeDate.text());
            waybillNoticeHistory.setNotifyDetail(part1_flightNumber.text() + " from " + part1_origin.text() + " to " + part1_destination.text() + " " + active.text());
            waybillNoticeHistory.setWaybillNo(waybillInfo.getWaybillNo());
            waybillNotice.toNotify(waybillNoticeHistory);
        }

        Element child_detail = container.select("> table:nth-child(5) > tbody").get(0);

        int children_size = child_detail.children().size();
        for (int line = 1; line <= children_size; line = line + 2) {
            Element child_detail_column = child_detail.select("> tr:nth-child(" + line + ")").get(0);
            Element origin_column = child_detail_column.select("> td:nth-child(2)").get(0);
            Element destination_column = child_detail_column.select("> td:nth-child(3)").get(0);
            Element flightNumber_column = child_detail_column.select("> td:nth-child(4)").get(0);
            Element flightDate_column = child_detail_column.select("> td:nth-child(5)").get(0);
            Element numberOfPieces_column = child_detail_column.select("> td:nth-child(6)").get(0);
            Element part1_weight_column = child_detail_column.select("> td:nth-child(7)").get(0);
            Element child_list = child_detail.select("> tr:nth-child(" + (line + 1) + ") > td:nth-child(2) > table > tbody").get(0);
            System.out.println("children_" + (line));
            Elements list_2 = child_list.children();
            Collections.reverse(list_2);
            for (Element element : list_2) {
                Element active = element.select("> td:nth-child(1)").get(0);
                Element activeDate = element.select("> td:nth-child(2)").get(0);
                System.out.println(element.text());
                WaybillNoticeHistory waybillNoticeHistory = new WaybillNoticeHistory();
                waybillNoticeHistory.setFlightNumber(flightNumber_column.text());
//            waybillNoticeHistory.setStatus();
                waybillNoticeHistory.setNotifyData(activeDate.text());
                waybillNoticeHistory.setNotifyDetail(flightNumber_column.text() + " from " + origin_column.text() + " to " + destination_column.text() + " " + active.text());
                waybillNoticeHistory.setWaybillNo(waybillInfo.getWaybillNo());
                waybillNotice.toNotify(waybillNoticeHistory);
            }
        }
    }


    public static void main(String[] args) {
        WaybillInfo waybillInfo = new WaybillInfo();
        waybillInfo.setWaybillNo("071-40970635");
//        waybillTrack(waybillInfo);
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
            log.info("http 获取运单为【{}】的信息", waybillNo);
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
        log.info("http 获取运单为【{}】的信息为{}", waybillNo, result);

        return result;
    }


}
