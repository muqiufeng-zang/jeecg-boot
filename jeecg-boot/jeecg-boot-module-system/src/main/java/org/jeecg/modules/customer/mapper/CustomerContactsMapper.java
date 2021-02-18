package org.jeecg.modules.customer.mapper;

import java.util.List;
import org.jeecg.modules.customer.entity.CustomerContacts;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 客户联系人
 * @Author: jeecg-boot
 * @Date:   2021-02-18
 * @Version: V1.0
 */
public interface CustomerContactsMapper extends BaseMapper<CustomerContacts> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<CustomerContacts> selectByMainId(@Param("mainId") String mainId);
}
