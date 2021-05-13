package org.jeecg.task.track;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
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
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
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
        //目的港
        String destination = summary.children().get(0).children().get(0).children().get(5).text();
//        Integer waybillStateCode = WaybillStateEn.toEnum(waybillState).getCode();
        waybillInfo.setDestination(destination);
//        waybillInfo.setWaybillSate(waybillStateCode);
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

                WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
                WxMpTemplateData first = new WxMpTemplateData("first", "");
                WxMpTemplateData keyword1 = new WxMpTemplateData("keyword1", "");
                WxMpTemplateData keyword2 = new WxMpTemplateData("keyword2", "");
                WxMpTemplateData keyword3 = new WxMpTemplateData("keyword3", "");
                WxMpTemplateData keyword4 = new WxMpTemplateData("keyword4", "");
                WxMpTemplateData remark = new WxMpTemplateData("remark", "");
                List<WxMpTemplateData> data = new ArrayList<>();
                data.add(first);
                data.add(keyword1);
                data.add(keyword2);
                data.add(keyword3);
                data.add(keyword4);
                data.add(remark);
                templateMessage.setData(data);
                templateMessage.setTemplateId(WxMpTemplateMessageEnum.TRACK.getId());
                templateMessage.setToUser("oHhp86cmK5egpnGFB553JsfRTdF0");
                WxMpService service = new WxMpServiceImpl();

//                try {
//                    service.getTemplateMsgService().sendTemplateMsg(templateMessage);
//                } catch (WxErrorException e) {
//                    e.printStackTrace();
//                }
                LambdaQueryWrapper<WaybillInfo> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper1.eq(WaybillInfo::getWaybillNo, waybillInfo.getWaybillNo());
                WaybillInfo waybillInfo1 = waybillInfoMapper.selectOne(lambdaQueryWrapper1);
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
                waybillNoticeHistory.setWaybillInfoId(waybillInfo1.getId());
                waybillNoticeHistoryMapper.insert(waybillNoticeHistory);
            }
        }
    }
}
