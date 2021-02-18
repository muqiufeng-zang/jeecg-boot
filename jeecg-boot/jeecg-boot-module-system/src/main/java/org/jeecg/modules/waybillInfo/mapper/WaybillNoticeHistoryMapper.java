package org.jeecg.modules.waybillInfo.mapper;

import java.util.List;
import org.jeecg.modules.waybillInfo.entity.WaybillNoticeHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 运单通知历史
 * @Author: jeecg-boot
 * @Date:   2021-01-27
 * @Version: V1.0
 */
public interface WaybillNoticeHistoryMapper extends BaseMapper<WaybillNoticeHistory> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<WaybillNoticeHistory> selectByMainId(@Param("mainId") String mainId);
}
