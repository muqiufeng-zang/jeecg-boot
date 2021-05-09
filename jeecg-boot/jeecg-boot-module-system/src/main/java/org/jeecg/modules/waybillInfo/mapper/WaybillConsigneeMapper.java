package org.jeecg.modules.waybillInfo.mapper;

import java.util.List;
import org.jeecg.modules.waybillInfo.entity.WaybillConsignee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 运单收货人
 * @Author: jeecg-boot
 * @Date:   2021-05-09
 * @Version: V1.0
 */
public interface WaybillConsigneeMapper extends BaseMapper<WaybillConsignee> {

	public boolean deleteByMainId(@Param("mainId") String mainId);

	public List<WaybillConsignee> selectByMainId(@Param("mainId") String mainId);
}
