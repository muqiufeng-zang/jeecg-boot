package org.jeecg.modules.customer.vo;

import java.util.List;

import org.jeecg.modules.customer.entity.CustomerContacts;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 客户信息
 * @Author: jeecg-boot
 * @Date:   2021-03-26
 * @Version: V1.0
 */
@Data
@ApiModel(value="customer_infoPage对象", description="客户信息")
public class CustomerInfoPage {

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
	/**公司名称*/
	@Excel(name = "公司名称", width = 15)
	@ApiModelProperty(value = "公司名称")
    private java.lang.String companyName;
	/**公司地址*/
	@Excel(name = "公司地址", width = 15)
	@ApiModelProperty(value = "公司地址")
    private java.lang.String companyAddress;

	@ExcelCollection(name="客户联系人")
	@ApiModelProperty(value = "客户联系人")
	private List<CustomerContacts> customerContactsList;

}
