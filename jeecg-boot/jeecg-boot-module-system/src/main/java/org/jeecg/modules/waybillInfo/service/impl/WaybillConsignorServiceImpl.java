package org.jeecg.modules.waybillInfo.service.impl;

import org.jeecg.modules.waybillInfo.entity.WaybillConsignor;
import org.jeecg.modules.waybillInfo.mapper.WaybillConsignorMapper;
import org.jeecg.modules.waybillInfo.service.IWaybillConsignorService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 运单发货人
 * @Author: jeecg-boot
 * @Date:   2021-02-19
 * @Version: V1.0
 */
@Service
public class WaybillConsignorServiceImpl extends ServiceImpl<WaybillConsignorMapper, WaybillConsignor> implements IWaybillConsignorService {
	
	@Autowired
	private WaybillConsignorMapper waybillConsignorMapper;
	
	@Override
	public List<WaybillConsignor> selectByMainId(String mainId) {
		return waybillConsignorMapper.selectByMainId(mainId);
	}
}
