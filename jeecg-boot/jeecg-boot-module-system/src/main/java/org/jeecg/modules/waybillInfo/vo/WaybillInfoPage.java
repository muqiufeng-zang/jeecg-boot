package org.jeecg.modules.waybillInfo.vo;

import java.util.List;

import org.jeecg.modules.waybillInfo.entity.WaybillConsignor;
import org.jeecg.modules.waybillInfo.entity.WaybillConsignee;
import org.jeecg.modules.waybillInfo.entity.WaybillNotice;
import org.jeecg.modules.waybillInfo.entity.WaybillNoticeHistory;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 运单信息表
 * @Author: jeecg-boot
 * @Date:   2021-02-18
 * @Version: V1.0
 */
@Data
@ApiModel(value="waybill_infoPage对象", description="运单信息表")
public class WaybillInfoPage {

	/**主键*/
	@ApiModelProperty(value = "主键")
	private java.lang.String id;
	/**创建人*/
	@ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建日期")
	private java.util.Date createTime;
	/**更新人*/
	@ApiModelProperty(value = "更新人")
	private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "更新日期")
	private java.util.Date updateTime;
	/**所属部门*/
	@ApiModelProperty(value = "所属部门")
	private java.lang.String sysOrgCode;
	/**运单号*/
	@Excel(name = "运单号", width = 15)
	@ApiModelProperty(value = "运单号")
	private java.lang.String waybillNo;
	/**运单状态*/
	@Excel(name = "运单状态", width = 15, dicCode = "waybill_state_dict")
    @Dict(dicCode = "waybill_state_dict")
	@ApiModelProperty(value = "运单状态")
	private java.lang.Integer waybillSate;

	@ExcelCollection(name="运单发货人")
	@ApiModelProperty(value = "运单发货人")
	private List<WaybillConsignor> waybillConsignorList;
	@ExcelCollection(name="运单收货人")
	@ApiModelProperty(value = "运单收货人")
	private List<WaybillConsignee> waybillConsigneeList;
	@ExcelCollection(name="运单状态通知人")
	@ApiModelProperty(value = "运单状态通知人")
	private List<WaybillNotice> waybillNoticeList;
	@ExcelCollection(name="运单通知历史")
	@ApiModelProperty(value = "运单通知历史")
	private List<WaybillNoticeHistory> waybillNoticeHistoryList;

}
