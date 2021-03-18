package org.jeecg.modules.waybillInfo.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 运单信息表
 * @Author: jeecg-boot
 * @Date:   2021-03-18
 * @Version: V1.0
 */
@ApiModel(value="waybill_info对象", description="运单信息表")
@Data
@TableName("waybill_info")
public class WaybillInfo implements Serializable {
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
	@Excel(name = "运单号", width = 15)
    @ApiModelProperty(value = "运单号")
    private String waybillNo;
	/**运单状态*/
	@Excel(name = "运单状态", width = 15, dicCode = "waybill_state_dict")
    @Dict(dicCode = "waybill_state_dict")
    @ApiModelProperty(value = "运单状态")
    private Integer waybillSate;
	/**始发地*/
	@Excel(name = "始发地", width = 15)
    @ApiModelProperty(value = "始发地")
    private String origin;
	/**目的地*/
	@Excel(name = "目的地", width = 15)
    @ApiModelProperty(value = "目的地")
    private String destination;
	/**件数*/
	@Excel(name = "件数", width = 15)
    @ApiModelProperty(value = "件数")
    private Integer pieces;
	/**体积*/
	@Excel(name = "体积", width = 15)
    @ApiModelProperty(value = "体积")
    private String volume;
	/**重量*/
	@Excel(name = "重量", width = 15)
    @ApiModelProperty(value = "重量")
    private String weight;
}
