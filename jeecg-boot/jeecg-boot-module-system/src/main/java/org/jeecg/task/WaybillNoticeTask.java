package org.jeecg.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.waybillInfo.entity.WaybillInfo;
import org.jeecg.modules.waybillInfo.mapper.WaybillInfoMapper;
import org.jeecg.modules.waybillInfo.mapper.WaybillNoticeMapper;
import org.jeecg.task.track.TrackFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
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
        log.info("当前运输中的运单有【{}】", Arrays.toString(waybillInfos.stream().map(WaybillInfo::getWaybillNo).toArray()));
        waybillInfos.forEach(waybillInfo -> {
            try {
                trackFactory.setWaybillInfo(waybillInfo).track();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }



}
