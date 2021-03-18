package org.jeecg.modules.waybillInfo.service;

import org.jeecg.modules.waybillInfo.entity.WaybillConsignor;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 运单发货人
 * @Author: jeecg-boot
 * @Date:   2021-03-18
 * @Version: V1.0
 */
public interface IWaybillConsignorService extends IService<WaybillConsignor> {

	public List<WaybillConsignor> selectByMainId(String mainId);
}
