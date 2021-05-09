package org.jeecg.modules.waybillInfo.service.impl;

import org.jeecg.modules.waybillInfo.entity.WaybillNotice;
import org.jeecg.modules.waybillInfo.mapper.WaybillNoticeMapper;
import org.jeecg.modules.waybillInfo.service.IWaybillNoticeService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 运单状态通知人
 * @Author: jeecg-boot
 * @Date:   2021-05-09
 * @Version: V1.0
 */
@Service
public class WaybillNoticeServiceImpl extends ServiceImpl<WaybillNoticeMapper, WaybillNotice> implements IWaybillNoticeService {

	@Autowired
	private WaybillNoticeMapper waybillNoticeMapper;

	@Override
	public List<WaybillNotice> selectByMainId(String mainId) {
		return waybillNoticeMapper.selectByMainId(mainId);
	}
}
