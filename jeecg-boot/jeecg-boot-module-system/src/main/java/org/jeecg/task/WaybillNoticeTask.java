package org.jeecg.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import okhttp3.internal.platform.OpenJSSEPlatform;
import org.jeecg.HttpClientUtil;
import org.jeecg.WaybillNotifyStateEn;
import org.jeecg.WaybillStateEn;
import org.jeecg.common.util.DySmsEnum;
import org.jeecg.common.util.DySmsHelper;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.waybillInfo.entity.WaybillInfo;
import org.jeecg.modules.waybillInfo.entity.WaybillNotice;
import org.jeecg.modules.waybillInfo.entity.WaybillNoticeHistory;
import org.jeecg.modules.waybillInfo.mapper.WaybillInfoMapper;
import org.jeecg.modules.waybillInfo.mapper.WaybillNoticeHistoryMapper;
import org.jeecg.modules.waybillInfo.mapper.WaybillNoticeMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: qiufeng
 * @Date: Created in 2021/2/19
 */

@Slf4j
@Component
public class WaybillNoticeTask {

    @Resource
    private WaybillNoticeMapper waybillNoticeMapper;

    @Resource
    private WaybillNoticeHistoryMapper waybillNoticeHistoryMapper;

    @Resource
    private WaybillInfoMapper waybillInfoMapper;


    /**
     * 每隔5秒一次
     */
    @Scheduled(cron = "*/5 * * * * ?")
    public void notice() {

        LambdaQueryWrapper<WaybillInfo> waybillInfoLambdaQueryWrapper = new LambdaQueryWrapper<WaybillInfo>();
        waybillInfoLambdaQueryWrapper.gt(WaybillInfo::getWaybillSate, 5)
                .lt(WaybillInfo::getWaybillSate, 100);

        List<WaybillInfo> waybillInfos = waybillInfoMapper.selectList(waybillInfoLambdaQueryWrapper);

        waybillInfos.forEach(waybillInfo -> {
            String url = "https://cargoserv.champ.aero/trace/trace.asp";
            String html = HttpClientUtil.doGet(url, "utf-8", "072-70872163");
            Document document = Jsoup.parse(html);
            Elements tables = document.body().getElementsByClass("summary");
            Element summary = tables.get(1);
            String waybillState = summary.children().get(0).children().get(0).children().get(2).text();
            Integer waybillStateCode = WaybillStateEn.toEnum(waybillState).getCode();
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
        });
//        log.info("运输中的运单：" + JSON.toJSONString(waybillInfos));
    }

    public Boolean invokeSendSMS(String waybillNo, String notifyData, String notifyDetail) {
        LambdaQueryWrapper<WaybillNotice> waybillNoticeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        waybillNoticeLambdaQueryWrapper.eq(WaybillNotice::getWaybillNo, waybillNo)
                .eq(WaybillNotice::getNotifyType, 1);
        List<WaybillNotice> waybillNotices = waybillNoticeMapper.selectList(waybillNoticeLambdaQueryWrapper);
        waybillNotices.forEach(waybillNotice -> {
            JSONObject templateParamJson = new JSONObject();
            templateParamJson.put("waybillNo",waybillNo);
            templateParamJson.put("notifyData",notifyData);
            templateParamJson.put("notifyDetail",notifyDetail);
            try {
                DySmsHelper.sendSms("15381048898", templateParamJson, DySmsEnum.REGISTER_TEMPLATE_CODE);
            } catch (ClientException e) {
                e.printStackTrace();
            }
        });

        return true;
    }

}
