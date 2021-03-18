package org.jeecg.modules.waybillInfo.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 运单通知历史
 * @Author: jeecg-boot
 * @Date:   2021-03-18
 * @Version: V1.0
 */
@ApiModel(value="waybill_info对象", description="运单信息表")
@Data
@TableName("waybill_notice_history")
public class WaybillNoticeHistory implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**运单号*/
    @ApiModelProperty(value = "运单号")
    private String waybillNo;
	/**批次*/
	@Excel(name = "批次", width = 15)
    @ApiModelProperty(value = "批次")
    private String part;
	/**通知状态*/
	@Excel(name = "通知状态", width = 15, dicCode = "waybill_notify_state_dict")
    @ApiModelProperty(value = "通知状态")
    private Integer notifyState;
	/**通知时间*/
	@Excel(name = "通知时间", width = 15)
    @ApiModelProperty(value = "通知时间")
    private String notifyData;
	/**航站*/
	@Excel(name = "航站", width = 15)
    @ApiModelProperty(value = "航站")
    private String station;
	/**航班号*/
	@Excel(name = "航班号", width = 15)
    @ApiModelProperty(value = "航班号")
    private String flightNumber;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private String status;
	/**通知详情*/
	@Excel(name = "通知详情", width = 15)
    @ApiModelProperty(value = "通知详情")
    private String notifyDetail;
}
