package org.jeecg.task;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.waybillInfo.entity.WaybillInfo;
import org.jeecg.modules.waybillInfo.entity.WaybillNotice;
import org.jeecg.modules.waybillInfo.mapper.WaybillInfoMapper;
import org.jeecg.modules.waybillInfo.mapper.WaybillNoticeMapper;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @Author: qiufeng
 * @Date: Created in 2021/2/19
 */

@Slf4j
public class WaybillNoticeTask {

    @Resource
    private WaybillNoticeMapper waybillNoticeMapper;

    @Resource
    private WaybillInfoMapper waybillInfoMapper;

    /**
     * 每隔5秒一次
     */
    @Scheduled(cron = "*/5 * * * * ?")
    public void notice() {

        QueryWrapper<WaybillInfo> queryWrapper = new QueryWrapper<WaybillInfo>()
                .between("waybillSate", 5, 100);

        List<WaybillInfo> waybillInfos = waybillInfoMapper.selectList(queryWrapper);

        log.info("运输中的运单：" + JSON.toJSONString(waybillInfos));
    }
}
