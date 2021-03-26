package org.jeecg.modules.customer.service;

import org.jeecg.modules.customer.entity.CustomerContacts;
import org.jeecg.modules.customer.entity.CustomerInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 客户信息
 * @Author: jeecg-boot
 * @Date:   2021-03-26
 * @Version: V1.0
 */
public interface ICustomerInfoService extends IService<CustomerInfo> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(CustomerInfo customerInfo,List<CustomerContacts> customerContactsList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(CustomerInfo customerInfo,List<CustomerContacts> customerContactsList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
