package org.jeecg.modules.waybillInfo.service;

import org.jeecg.modules.waybillInfo.entity.WaybillNotice;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 运单状态通知人
 * @Author: jeecg-boot
 * @Date:   2021-01-27
 * @Version: V1.0
 */
public interface IWaybillNoticeService extends IService<WaybillNotice> {

	public List<WaybillNotice> selectByMainId(String mainId);
}
