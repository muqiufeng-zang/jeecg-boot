package org.jeecg.modules.waybillInfo.service;

import org.jeecg.modules.waybillInfo.entity.WaybillConsignor;
import org.jeecg.modules.waybillInfo.entity.WaybillConsignee;
import org.jeecg.modules.waybillInfo.entity.WaybillNotice;
import org.jeecg.modules.waybillInfo.entity.WaybillNoticeHistory;
import org.jeecg.modules.waybillInfo.entity.WaybillInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 运单信息表
 * @Author: jeecg-boot
 * @Date:   2021-01-27
 * @Version: V1.0
 */
public interface IWaybillInfoService extends IService<WaybillInfo> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(WaybillInfo waybillInfo, List<WaybillConsignor> waybillConsignorList, List<WaybillConsignee> waybillConsigneeList, List<WaybillNotice> waybillNoticeList, List<WaybillNoticeHistory> waybillNoticeHistoryList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(WaybillInfo waybillInfo,List<WaybillConsignor> waybillConsignorList,List<WaybillConsignee> waybillConsigneeList,List<WaybillNotice> waybillNoticeList,List<WaybillNoticeHistory> waybillNoticeHistoryList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
