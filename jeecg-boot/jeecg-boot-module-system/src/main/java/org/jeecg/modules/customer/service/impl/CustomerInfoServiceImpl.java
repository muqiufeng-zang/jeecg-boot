package org.jeecg.modules.customer.service.impl;

import org.jeecg.modules.customer.entity.CustomerInfo;
import org.jeecg.modules.customer.entity.CustomerContacts;
import org.jeecg.modules.customer.mapper.CustomerContactsMapper;
import org.jeecg.modules.customer.mapper.CustomerInfoMapper;
import org.jeecg.modules.customer.service.ICustomerInfoService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 客户信息
 * @Author: jeecg-boot
 * @Date:   2021-03-26
 * @Version: V1.0
 */
@Service
public class CustomerInfoServiceImpl extends ServiceImpl<CustomerInfoMapper, CustomerInfo> implements ICustomerInfoService {

	@Autowired
	private CustomerInfoMapper customerInfoMapper;
	@Autowired
	private CustomerContactsMapper customerContactsMapper;
	
	@Override
	@Transactional
	public void saveMain(CustomerInfo customerInfo, List<CustomerContacts> customerContactsList) {
		customerInfoMapper.insert(customerInfo);
		if(customerContactsList!=null && customerContactsList.size()>0) {
			for(CustomerContacts entity:customerContactsList) {
				//外键设置
				entity.setCompanyId(customerInfo.getId());
				customerContactsMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(CustomerInfo customerInfo,List<CustomerContacts> customerContactsList) {
		customerInfoMapper.updateById(customerInfo);
		
		//1.先删除子表数据
		customerContactsMapper.deleteByMainId(customerInfo.getId());
		
		//2.子表数据重新插入
		if(customerContactsList!=null && customerContactsList.size()>0) {
			for(CustomerContacts entity:customerContactsList) {
				//外键设置
				entity.setCompanyId(customerInfo.getId());
				customerContactsMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		customerContactsMapper.deleteByMainId(id);
		customerInfoMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			customerContactsMapper.deleteByMainId(id.toString());
			customerInfoMapper.deleteById(id);
		}
	}
	
}
