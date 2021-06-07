package org.jeecg.task.track;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.WaybillNotifyStateEn;
import org.jeecg.modules.waybillInfo.entity.WaybillInfo;
import org.jeecg.modules.waybillInfo.entity.WaybillNoticeHistory;
import org.jeecg.modules.waybillInfo.mapper.WaybillInfoMapper;
import org.jeecg.modules.waybillInfo.mapper.WaybillNoticeHistoryMapper;
import org.jeecg.notify.MessageNotify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: qiufeng
 * @Date: Created in 2021/6/6
 */
@Component
public class WaybillNotice {

    @Resource
    private WaybillNoticeHistoryMapper waybillNoticeHistoryMapper;

    @Resource
    private WaybillInfoMapper waybillInfoMapper;

    @Autowired
    private MessageNotify messageNotify;

    public void toNotify(WaybillNoticeHistory waybillNoticeHistory) {

        //如果该行信息在数据库已存在，跳过这一行
        LambdaQueryWrapper<WaybillNoticeHistory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WaybillNoticeHistory::getNotifyDetail, waybillNoticeHistory.getNotifyDetail())
                .eq(WaybillNoticeHistory::getNotifyData, waybillNoticeHistory.getNotifyData());
        List<WaybillNoticeHistory> waybillNoticeHistories =
                waybillNoticeHistoryMapper.selectList(lambdaQueryWrapper);
        if (waybillNoticeHistories.size() > 0) {
            return;
        }

        boolean notifySuccess = messageNotify.toNotify(waybillNoticeHistory.getStatus(), waybillNoticeHistory.getWaybillNo(), waybillNoticeHistory.getNotifyData(), waybillNoticeHistory.getNotifyDetail(), null);
        if (!notifySuccess) {
            waybillNoticeHistory.setNotifyState(WaybillNotifyStateEn.FAIL.getCode());
        }

        waybillNoticeHistory.setFlightNumber(waybillNoticeHistory.getFlightNumber());
        waybillNoticeHistory.setWaybillNo(waybillNoticeHistory.getWaybillNo());
        waybillNoticeHistory.setNotifyDetail(waybillNoticeHistory.getNotifyDetail());
        waybillNoticeHistory.setNotifyData(waybillNoticeHistory.getNotifyData());
        waybillNoticeHistory.setNotifyState(WaybillNotifyStateEn.NO_NOTIFY.getCode());
        waybillNoticeHistory.setCreateBy("system");
        waybillNoticeHistory.setCreateTime(new Date());
        waybillNoticeHistory.setUpdateBy("system");
        waybillNoticeHistory.setUpdateTime(new Date());
        LambdaQueryWrapper<WaybillInfo> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(WaybillInfo::getWaybillNo, waybillNoticeHistory.getWaybillNo());
        WaybillInfo waybillInfo1 = waybillInfoMapper.selectOne(lambdaQueryWrapper1);
        waybillNoticeHistory.setWaybillInfoId(waybillInfo1.getId());
        waybillNoticeHistoryMapper.insert(waybillNoticeHistory);
    }
}
