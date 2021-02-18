package org.jeecg.modules.waybillInfo.service.impl;

import org.jeecg.modules.waybillInfo.entity.WaybillConsignee;
import org.jeecg.modules.waybillInfo.mapper.WaybillConsigneeMapper;
import org.jeecg.modules.waybillInfo.service.IWaybillConsigneeService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 运单收货人
 * @Author: jeecg-boot
 * @Date:   2021-02-18
 * @Version: V1.0
 */
@Service
public class WaybillConsigneeServiceImpl extends ServiceImpl<WaybillConsigneeMapper, WaybillConsignee> implements IWaybillConsigneeService {
	
	@Autowired
	private WaybillConsigneeMapper waybillConsigneeMapper;
	
	@Override
	public List<WaybillConsignee> selectByMainId(String mainId) {
		return waybillConsigneeMapper.selectByMainId(mainId);
	}
}
