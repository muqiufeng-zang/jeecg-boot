package org.jeecg.modules.waybillInfo.service;

import org.jeecg.modules.waybillInfo.entity.WaybillConsignee;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 运单收货人
 * @Author: jeecg-boot
 * @Date:   2021-03-18
 * @Version: V1.0
 */
public interface IWaybillConsigneeService extends IService<WaybillConsignee> {

	public List<WaybillConsignee> selectByMainId(String mainId);
}
