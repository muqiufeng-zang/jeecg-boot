package org.jeecg.modules.waybillInfo.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.waybillInfo.entity.WaybillConsignor;

/**
 * @Description: 运单发货人
 * @Author: jeecg-boot
 * @Date:   2021-03-18
 * @Version: V1.0
 */
public interface WaybillConsignorMapper extends BaseMapper<WaybillConsignor> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<WaybillConsignor> selectByMainId(@Param("mainId") String mainId);
}
