package org.jeecg.modules.waybillInfo.service.impl;

import org.jeecg.modules.waybillInfo.entity.WaybillNoticeHistory;
import org.jeecg.modules.waybillInfo.mapper.WaybillNoticeHistoryMapper;
import org.jeecg.modules.waybillInfo.service.IWaybillNoticeHistoryService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 运单通知历史
 * @Author: jeecg-boot
 * @Date:   2021-02-18
 * @Version: V1.0
 */
@Service
public class WaybillNoticeHistoryServiceImpl extends ServiceImpl<WaybillNoticeHistoryMapper, WaybillNoticeHistory> implements IWaybillNoticeHistoryService {
	
	@Autowired
	private WaybillNoticeHistoryMapper waybillNoticeHistoryMapper;
	
	@Override
	public List<WaybillNoticeHistory> selectByMainId(String mainId) {
		return waybillNoticeHistoryMapper.selectByMainId(mainId);
	}
}
