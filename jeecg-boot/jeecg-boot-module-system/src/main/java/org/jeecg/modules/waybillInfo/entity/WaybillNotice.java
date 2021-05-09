package org.jeecg.modules.waybillInfo.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 运单状态通知人
 * @Author: jeecg-boot
 * @Date:   2021-05-09
 * @Version: V1.0
 */
@ApiModel(value="waybill_info对象", description="运单信息表")
@Data
@TableName("waybill_notice")
public class WaybillNotice implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
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
	/**接受通知人*/
	@Excel(name = "接受通知人", width = 15, dictTable = "customer_contacts", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "接受通知人")
    private java.lang.String userId;
	/**通知类型*/
	@Excel(name = "通知类型", width = 15, dicCode = "notify_type_dict")
    @ApiModelProperty(value = "通知类型")
    private java.lang.Integer notifyType;
	/**外键*/
    @ApiModelProperty(value = "外键")
    private java.lang.String waybillInfoId;
}
