package org.jeecg.task.track;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.en.FlightInfoEnum;
import org.jeecg.modules.waybillInfo.entity.WaybillInfo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description: 物流跟踪工厂
 * @Author: qiufeng
 * @Date: Created in 2021/3/2
 */
@Slf4j
@Component
public class TrackFactory {

    @Resource
    private GFA gfa;

    @Resource
    public TrackAbstract trackAbstract;

    public WaybillInfo waybillInfo;

    public TrackFactory setWaybillInfo(WaybillInfo waybillInfo) {
        this.waybillInfo = waybillInfo;
        return this;
    }

    public void track() throws Exception {
        String waybillNo = waybillInfo.getWaybillNo();
        String threeCode = waybillNo.split("-")[0];
        switch (FlightInfoEnum.toNum(threeCode)) {
            case GFA:
                trackAbstract = gfa;
                break;
            default:
                trackAbstract = null;
                log.info("当前运单【{}】没有匹配的航司，请管理员尽快添加！", waybillNo);
                return;
        }

        trackAbstract.waybillTrack(waybillInfo);
    }
}
