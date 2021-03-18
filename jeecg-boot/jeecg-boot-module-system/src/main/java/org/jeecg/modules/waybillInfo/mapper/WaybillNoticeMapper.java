package org.jeecg.modules.waybillInfo.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.waybillInfo.entity.WaybillNotice;

/**
 * @Description: 运单状态通知人
 * @Author: jeecg-boot
 * @Date:   2021-03-18
 * @Version: V1.0
 */
public interface WaybillNoticeMapper extends BaseMapper<WaybillNotice> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<WaybillNotice> selectByMainId(@Param("mainId") String mainId);
}
