package org.jeecg.modules.waybillInfo.service;

import org.jeecg.modules.waybillInfo.entity.WaybillNoticeHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 运单通知历史
 * @Author: jeecg-boot
 * @Date:   2021-03-18
 * @Version: V1.0
 */
public interface IWaybillNoticeHistoryService extends IService<WaybillNoticeHistory> {

	public List<WaybillNoticeHistory> selectByMainId(String mainId);
}
