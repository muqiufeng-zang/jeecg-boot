package org.jeecg.task;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DySmsEnum;
import org.jeecg.common.util.DySmsHelper;
import org.jeecg.modules.waybillInfo.entity.WaybillInfo;
import org.jeecg.modules.waybillInfo.entity.WaybillNotice;
import org.jeecg.modules.waybillInfo.mapper.WaybillInfoMapper;
import org.jeecg.modules.waybillInfo.mapper.WaybillNoticeMapper;
import org.jeecg.task.track.TrackFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
    private WaybillInfoMapper waybillInfoMapper;

    @Resource
    private TrackFactory trackFactory;


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
            try {
                trackFactory.setWaybillInfo(waybillInfo).track();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Boolean invokeSendSMS(String waybillNo, String notifyData, String notifyDetail) {
        LambdaQueryWrapper<WaybillNotice> waybillNoticeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        waybillNoticeLambdaQueryWrapper.eq(WaybillNotice::getWaybillNo, waybillNo)
                .eq(WaybillNotice::getNotifyType, 1);
        List<WaybillNotice> waybillNotices = waybillNoticeMapper.selectList(waybillNoticeLambdaQueryWrapper);
        waybillNotices.forEach(waybillNotice -> {
            JSONObject templateParamJson = new JSONObject();
            templateParamJson.put("waybillNo", waybillNo);
            templateParamJson.put("notifyData", notifyData);
            templateParamJson.put("notifyDetail", notifyDetail);
            try {
                DySmsHelper.sendSms("15381048898", templateParamJson, DySmsEnum.REGISTER_TEMPLATE_CODE);
            } catch (ClientException e) {
                e.printStackTrace();
            }
        });

        return true;
    }

}
