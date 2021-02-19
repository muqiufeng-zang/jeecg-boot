package org.jeecg.modules.waybillInfo.mapper;

import java.util.List;
import org.jeecg.modules.waybillInfo.entity.WaybillNotice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 运单状态通知人
 * @Author: jeecg-boot
 * @Date:   2021-02-19
 * @Version: V1.0
 */
public interface WaybillNoticeMapper extends BaseMapper<WaybillNotice> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<WaybillNotice> selectByMainId(@Param("mainId") String mainId);
}
