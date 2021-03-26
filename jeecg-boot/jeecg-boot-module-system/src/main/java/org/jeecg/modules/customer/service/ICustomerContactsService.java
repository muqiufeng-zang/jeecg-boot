package org.jeecg.modules.customer.service;

import org.jeecg.modules.customer.entity.CustomerContacts;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 客户联系人
 * @Author: jeecg-boot
 * @Date:   2021-03-26
 * @Version: V1.0
 */
public interface ICustomerContactsService extends IService<CustomerContacts> {

	public List<CustomerContacts> selectByMainId(String mainId);
}
