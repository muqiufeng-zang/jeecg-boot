package org.jeecg.task.track;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.HttpClientUtil;
import org.jeecg.WaybillNotifyStateEn;
import org.jeecg.WaybillStateEn;
import org.jeecg.modules.waybillInfo.entity.WaybillInfo;
import org.jeecg.modules.waybillInfo.entity.WaybillNoticeHistory;
import org.jeecg.modules.waybillInfo.mapper.WaybillInfoMapper;
import org.jeecg.modules.waybillInfo.mapper.WaybillNoticeHistoryMapper;
import org.jeecg.notify.MessageNotify;
import org.jeecg.wx.mp.config.WxMpProperties;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: qiufeng
 * @Date: Created in 2021/3/2
 */
@Primary
@Slf4j
@Component
@AllArgsConstructor
@Configuration
@EnableConfigurationProperties(WxMpProperties.class)
public class GFA extends TrackAbstract {

    @Resource
    private WaybillNoticeHistoryMapper waybillNoticeHistoryMapper;

    @Resource
    private WaybillInfoMapper waybillInfoMapper;

    @Autowired
    private MessageNotify messageNotify;

    @Override
    public void waybillTrack(WaybillInfo waybillInfo) {
        String url = "http://www.cargo.sita.aero/trace/trace.asp";
        String html = HttpClientUtil.doGet(url, "utf-8", waybillInfo.getWaybillNo());
        Document document = Jsoup.parse(html);
        Elements tables = document.body().getElementsByClass("summary");
        if (tables.size() < 1) {
            log.error("运单【{}】查询失败！查询结果如下：/n/r {}", waybillInfo.getWaybillNo(), html);
            return;
        }
        Element summary = tables.get(1);
        //运单号
        String waybillNo = summary.children().get(0).children().get(0).children().get(0).text();
        //运单批次
        String part = summary.children().get(0).children().get(0).children().get(1).text();
        //运单状态
        String waybillState = summary.children().get(0).children().get(0).children().get(2).text();
        //航空公司
        String flight = summary.children().get(0).children().get(0).children().get(3).text();
        if (flight.contains("Could not find")) {
            log.error("运单【{}】不存在，查询不到物流信息", waybillNo);
            return;
        }
        //目的港
        String destination = summary.children().get(0).children().get(0).children().get(5).text();
        Integer waybillStateCode = WaybillStateEn.toEnum(waybillState).getCode();
        waybillInfo.setPieces(Integer.valueOf(part));
        waybillInfo.setDestination(destination);
        waybillInfo.setWaybillSate(waybillStateCode);
        waybillInfoMapper.updateById(waybillInfo);
        Elements detail = document.getElementsByClass("detail");
        for (Element element : detail) {
            Elements trs = element.select("tr");
            Collections.reverse(trs);
            for (Element tr : trs) {
                Elements tds = tr.select("td");
                String notifyDetail = tds.get(0).text();
                String notifyDate = tds.get(1).text();

                //如果行信息为空，跳过这一行
                if (tds.size() < 2 || notifyDetail.isEmpty() || notifyDate.isEmpty()) {
                    continue;
                }

                //如果该行信息在数据库已存在，跳过这一行
                LambdaQueryWrapper<WaybillNoticeHistory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(WaybillNoticeHistory::getNotifyDetail, notifyDetail)
                        .eq(WaybillNoticeHistory::getNotifyData, notifyDate);
                List<WaybillNoticeHistory> waybillNoticeHistories =
                        waybillNoticeHistoryMapper.selectList(lambdaQueryWrapper);
                if (waybillNoticeHistories.size() > 0) {
                    continue;
                }

                boolean notifySuccess = messageNotify.toNotify(waybillState, waybillNo, notifyDate, notifyDetail, null);
                WaybillNoticeHistory waybillNoticeHistory = new WaybillNoticeHistory();
                if (!notifySuccess) {
                    waybillNoticeHistory.setNotifyState(WaybillNotifyStateEn.FAIL.getCode());
                }

                waybillNoticeHistory.setFlightNumber(flight);
                waybillNoticeHistory.setWaybillNo(waybillInfo.getWaybillNo());
                waybillNoticeHistory.setNotifyDetail(notifyDetail);
                waybillNoticeHistory.setNotifyData(notifyDate);
                waybillNoticeHistory.setNotifyState(WaybillNotifyStateEn.NO_NOTIFY.getCode());
                waybillNoticeHistory.setCreateBy("system");
                waybillNoticeHistory.setCreateTime(new Date());
                waybillNoticeHistory.setUpdateBy("system");
                waybillNoticeHistory.setUpdateTime(new Date());
                LambdaQueryWrapper<WaybillInfo> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper1.eq(WaybillInfo::getWaybillNo, waybillInfo.getWaybillNo());
                WaybillInfo waybillInfo1 = waybillInfoMapper.selectOne(lambdaQueryWrapper1);
                waybillNoticeHistory.setWaybillInfoId(waybillInfo1.getId());
                waybillNoticeHistoryMapper.insert(waybillNoticeHistory);
            }
        }
    }
}
