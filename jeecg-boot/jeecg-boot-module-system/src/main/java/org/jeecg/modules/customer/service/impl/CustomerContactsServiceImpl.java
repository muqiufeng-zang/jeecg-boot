package org.jeecg.modules.customer.service.impl;

import org.jeecg.modules.customer.entity.CustomerContacts;
import org.jeecg.modules.customer.mapper.CustomerContactsMapper;
import org.jeecg.modules.customer.service.ICustomerContactsService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 客户联系人
 * @Author: jeecg-boot
 * @Date:   2021-02-18
 * @Version: V1.0
 */
@Service
public class CustomerContactsServiceImpl extends ServiceImpl<CustomerContactsMapper, CustomerContacts> implements ICustomerContactsService {
	
	@Autowired
	private CustomerContactsMapper customerContactsMapper;
	
	@Override
	public List<CustomerContacts> selectByMainId(String mainId) {
		return customerContactsMapper.selectByMainId(mainId);
	}
}
