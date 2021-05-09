package org.jeecg.modules.waybillInfo.service.impl;

import org.jeecg.modules.waybillInfo.entity.WaybillInfo;
import org.jeecg.modules.waybillInfo.entity.WaybillConsignor;
import org.jeecg.modules.waybillInfo.entity.WaybillConsignee;
import org.jeecg.modules.waybillInfo.entity.WaybillNotice;
import org.jeecg.modules.waybillInfo.entity.WaybillNoticeHistory;
import org.jeecg.modules.waybillInfo.mapper.WaybillConsignorMapper;
import org.jeecg.modules.waybillInfo.mapper.WaybillConsigneeMapper;
import org.jeecg.modules.waybillInfo.mapper.WaybillNoticeMapper;
import org.jeecg.modules.waybillInfo.mapper.WaybillNoticeHistoryMapper;
import org.jeecg.modules.waybillInfo.mapper.WaybillInfoMapper;
import org.jeecg.modules.waybillInfo.service.IWaybillInfoService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 运单信息表
 * @Author: jeecg-boot
 * @Date:   2021-05-09
 * @Version: V1.0
 */
@Service
public class WaybillInfoServiceImpl extends ServiceImpl<WaybillInfoMapper, WaybillInfo> implements IWaybillInfoService {

	@Autowired
	private WaybillInfoMapper waybillInfoMapper;
	@Autowired
	private WaybillConsignorMapper waybillConsignorMapper;
	@Autowired
	private WaybillConsigneeMapper waybillConsigneeMapper;
	@Autowired
	private WaybillNoticeMapper waybillNoticeMapper;
	@Autowired
	private WaybillNoticeHistoryMapper waybillNoticeHistoryMapper;

	@Override
	@Transactional
	public void saveMain(WaybillInfo waybillInfo, List<WaybillConsignor> waybillConsignorList,List<WaybillConsignee> waybillConsigneeList,List<WaybillNotice> waybillNoticeList,List<WaybillNoticeHistory> waybillNoticeHistoryList) {
		waybillInfoMapper.insert(waybillInfo);
		if(waybillConsignorList!=null && waybillConsignorList.size()>0) {
			for(WaybillConsignor entity:waybillConsignorList) {
				//外键设置
				entity.setWaybillInfoId(waybillInfo.getId());
				waybillConsignorMapper.insert(entity);
			}
		}
		if(waybillConsigneeList!=null && waybillConsigneeList.size()>0) {
			for(WaybillConsignee entity:waybillConsigneeList) {
				//外键设置
				entity.setWaybillInfoId(waybillInfo.getId());
				waybillConsigneeMapper.insert(entity);
			}
		}
		if(waybillNoticeList!=null && waybillNoticeList.size()>0) {
			for(WaybillNotice entity:waybillNoticeList) {
				//外键设置
				entity.setWaybillInfoId(waybillInfo.getId());
				waybillNoticeMapper.insert(entity);
			}
		}
		if(waybillNoticeHistoryList!=null && waybillNoticeHistoryList.size()>0) {
			for(WaybillNoticeHistory entity:waybillNoticeHistoryList) {
				//外键设置
				entity.setWaybillInfoId(waybillInfo.getId());
				waybillNoticeHistoryMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(WaybillInfo waybillInfo,List<WaybillConsignor> waybillConsignorList,List<WaybillConsignee> waybillConsigneeList,List<WaybillNotice> waybillNoticeList,List<WaybillNoticeHistory> waybillNoticeHistoryList) {
		waybillInfoMapper.updateById(waybillInfo);

		//1.先删除子表数据
		waybillConsignorMapper.deleteByMainId(waybillInfo.getId());
		waybillConsigneeMapper.deleteByMainId(waybillInfo.getId());
		waybillNoticeMapper.deleteByMainId(waybillInfo.getId());
		waybillNoticeHistoryMapper.deleteByMainId(waybillInfo.getId());

		//2.子表数据重新插入
		if(waybillConsignorList!=null && waybillConsignorList.size()>0) {
			for(WaybillConsignor entity:waybillConsignorList) {
				//外键设置
				entity.setWaybillInfoId(waybillInfo.getId());
				waybillConsignorMapper.insert(entity);
			}
		}
		if(waybillConsigneeList!=null && waybillConsigneeList.size()>0) {
			for(WaybillConsignee entity:waybillConsigneeList) {
				//外键设置
				entity.setWaybillInfoId(waybillInfo.getId());
				waybillConsigneeMapper.insert(entity);
			}
		}
		if(waybillNoticeList!=null && waybillNoticeList.size()>0) {
			for(WaybillNotice entity:waybillNoticeList) {
				//外键设置
				entity.setWaybillInfoId(waybillInfo.getId());
				waybillNoticeMapper.insert(entity);
			}
		}
		if(waybillNoticeHistoryList!=null && waybillNoticeHistoryList.size()>0) {
			for(WaybillNoticeHistory entity:waybillNoticeHistoryList) {
				//外键设置
				entity.setWaybillInfoId(waybillInfo.getId());
				waybillNoticeHistoryMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		waybillConsignorMapper.deleteByMainId(id);
		waybillConsigneeMapper.deleteByMainId(id);
		waybillNoticeMapper.deleteByMainId(id);
		waybillNoticeHistoryMapper.deleteByMainId(id);
		waybillInfoMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			waybillConsignorMapper.deleteByMainId(id.toString());
			waybillConsigneeMapper.deleteByMainId(id.toString());
			waybillNoticeMapper.deleteByMainId(id.toString());
			waybillNoticeHistoryMapper.deleteByMainId(id.toString());
			waybillInfoMapper.deleteById(id);
		}
	}

}
