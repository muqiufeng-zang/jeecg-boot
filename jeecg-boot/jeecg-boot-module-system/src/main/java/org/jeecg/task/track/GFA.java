package org.jeecg.task.track;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.HttpClientUtil;
import org.jeecg.WaybillNotifyStateEn;
import org.jeecg.WaybillStateEn;
import org.jeecg.modules.waybillInfo.entity.WaybillInfo;
import org.jeecg.modules.waybillInfo.entity.WaybillNoticeHistory;
import org.jeecg.modules.waybillInfo.mapper.WaybillInfoMapper;
import org.jeecg.modules.waybillInfo.mapper.WaybillNoticeHistoryMapper;
import org.jeecg.modules.waybillInfo.mapper.WaybillNoticeMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
public class GFA extends TrackAbstract {

    @Resource
    private WaybillNoticeHistoryMapper waybillNoticeHistoryMapper;

    @Resource
    private WaybillInfoMapper waybillInfoMapper;

    @Override
    public void waybillTrack(WaybillInfo waybillInfo) {
        String url = "https://cargoserv.champ.aero/trace/trace.asp";
        String html = HttpClientUtil.doGet(url, "utf-8", waybillInfo.getWaybillNo());
        Document document = Jsoup.parse(html);
        Elements tables = document.body().getElementsByClass("summary");
        Element summary = tables.get(1);
        //运单号
        String waybillNo = summary.children().get(0).children().get(0).children().get(0).text();
        //运单批次
        String part = summary.children().get(0).children().get(0).children().get(1).text();
        //运单状态
        String waybillState = summary.children().get(0).children().get(0).children().get(2).text();
        //航空公司
        String flight = summary.children().get(0).children().get(0).children().get(3).text();
        //目的港
        String destination = summary.children().get(0).children().get(0).children().get(4).text();
        Integer waybillStateCode = WaybillStateEn.toEnum(waybillState).getCode();
        waybillInfo.setDestination(destination);
        waybillInfo.setWaybillSate(waybillStateCode);
        waybillInfoMapper.updateById(waybillInfo);
        Elements detail = document.getElementsByClass("detail");
        for (Element element : detail) {
            Elements trs = element.select("tr");
            for (Element tr : trs) {
                Elements tds = tr.select("td");
//                    System.out.println("内容：" + tds.get(0).text() + "时间：" + tds.get(1).text());
                if (tds.size() < 2 || tds.get(0).text().isEmpty() || tds.get(1).text().isEmpty()) {
                    continue;
                }

                LambdaQueryWrapper<WaybillNoticeHistory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(WaybillNoticeHistory::getNotifyDetail, tds.get(0).text())
                        .eq(WaybillNoticeHistory::getNotifyData, tds.get(1).text());

                List<WaybillNoticeHistory> waybillNoticeHistories =
                        waybillNoticeHistoryMapper.selectList(lambdaQueryWrapper);
                if (waybillNoticeHistories.size() > 0) {
                    continue;
                }
//                    Boolean success = invokeSendSMS(waybillInfo.getWaybillNo(), tds.get(1).text(), tds.get(0).text());

                WaybillNoticeHistory waybillNoticeHistory = new WaybillNoticeHistory();
//                    if (!success) {
//                        waybillNoticeHistory.setNotifyState(WaybillNotifyStateEn.FAIL.getCode());
//                    }

                waybillNoticeHistory.setFlightNumber(flight);
                waybillNoticeHistory.setWaybillNo(waybillInfo.getWaybillNo());
                waybillNoticeHistory.setNotifyDetail(tds.get(0).text());
                waybillNoticeHistory.setNotifyData(tds.get(1).text());
                waybillNoticeHistory.setNotifyState(WaybillNotifyStateEn.NO_NOTIFY.getCode());
                waybillNoticeHistory.setCreateBy("system");
                waybillNoticeHistory.setCreateTime(new Date());
                waybillNoticeHistory.setUpdateBy("system");
                waybillNoticeHistory.setUpdateTime(new Date());
                waybillNoticeHistoryMapper.insert(waybillNoticeHistory);
            }
        }
    }
}
